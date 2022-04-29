package com.biaoge.vuespringboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.biaoge.vuespringboot.common.Constants;
import com.biaoge.vuespringboot.common.Result;
import com.biaoge.vuespringboot.controller.dto.UserDTO;
import com.biaoge.vuespringboot.entity.User;

import com.biaoge.vuespringboot.service.IUserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    private IUserService userService;

    //查询
    @GetMapping
    public List<User> findAll(){
        return userService.list();
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        String username=userDTO.getUsername();
        String password= userDTO.getPassword();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto=userService.login(userDTO);
        return  Result.success(dto);
    }

    @PostMapping
    public boolean save(@RequestBody User user){
        //新增或者更新
       return  userService.saveOrUpdate(user);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
        return userService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return userService.removeByIds(ids);
    }

    //分页查询
    //@RequestParam 接收参数
    //接口路径/user/page?pageNum=1&pageSize=10
    //limit第一个参数=(pageNum-1)*pageSize
//    @GetMapping("/page")
//    public Map<String, Object> findPage(@RequestParam("pageNum") Integer pageNum,
//                                        @RequestParam("pageSize") Integer pageSize,
//                                        @RequestParam("username") String username,
//                                        @RequestParam("email") String email,
//                                        @RequestParam("address") String address){
//        pageNum=(pageNum - 1) * pageSize;
//        //查询总条数
//        List<User> data=userMapper.selectPage(pageNum,pageSize,username,email,address);
//        Integer total= userMapper.selectTotal(username,email,address);
//        Map<String,Object> res=new HashMap<>();
//        res.put("data",data);
//        res.put("total",total);
//        return res;
//    }

    //分页查询- mybatis-plus的方法
    @GetMapping("/page")
    public IPage<User> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String username,
                                @RequestParam(defaultValue = "") String email,
                                @RequestParam(defaultValue = "") String address){
        IPage<User> page=new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if(!"".equals(username)){
            queryWrapper.like("username",username);
        }
        if(!"".equals(email)){
            queryWrapper.like("email",email);
        }
        if(!"".equals(address)){
            queryWrapper.like("address",address);
        }
        queryWrapper.orderByDesc("id");
        return userService.page(page,queryWrapper);
    }

    /**
     * Excel导出数据接口
     * @param response
     * @throws Exception
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库查询出所有数据
        List<User> list=userService.list();
        //通过工具类创建writer 写出到磁盘路径
        //ExcelWriter writer=ExcelUtil.getWriter(fileUploadPath+"/用户信息.xlsx");
        //通过内存操作，写到浏览器
        ExcelWriter writer= ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("username","用户名");
        writer.addHeaderAlias("password","密码");
        writer.addHeaderAlias("nickname","昵称");
        writer.addHeaderAlias("email","邮箱");
        writer.addHeaderAlias("phone","电话号码");
        writer.addHeaderAlias("address","地址");
        writer.addHeaderAlias("createTime","创建时间");
        writer.addHeaderAlias("avatarUrl","头像");

        //一次性写出list内的对象到excel,使用默认样式，强制输出标题
        writer.write(list,true);

        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName= URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        //输出流
        ServletOutputStream out=response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }


    /**
     * Excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception{
        InputStream inputStream= file.getInputStream();
        ExcelReader reader=ExcelUtil.getReader(inputStream);

        //第一种写法，通过javaBean的方式读取Excel 的对象，但是表头要求是英文的，必须和javaBean的属性对应
        //List<User> list=reader.readAll(User.class);

        //第二种写法,忽略中文表头，可直接读取表格内容
        List<List<Object>> list=reader.read(1);
        List<User> users= CollUtil.newArrayList();
        for(List<Object> row:list){
            User user=new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            user.setAvatarUrl(row.get(6).toString());
            users.add(user);
        }

        //保存到数据库
        userService.saveBatch(users);
        return true;
    }
}
