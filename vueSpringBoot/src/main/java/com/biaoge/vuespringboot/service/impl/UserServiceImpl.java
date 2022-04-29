package com.biaoge.vuespringboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biaoge.vuespringboot.common.Constants;
import com.biaoge.vuespringboot.controller.dto.UserDTO;
import com.biaoge.vuespringboot.entity.User;
import com.biaoge.vuespringboot.exception.ServiceException;
import com.biaoge.vuespringboot.mapper.UserMapper;
import com.biaoge.vuespringboot.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = LogFactory.get(); //hutool的log
    @Override
    public UserDTO login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",userDTO.getUsername());
        queryWrapper.eq("password",userDTO.getPassword());
        User one;
        try{
            one=getOne(queryWrapper);//从数据库查询信息
        }catch(Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        if(one !=null){
            BeanUtil.copyProperties(one,userDTO,true); //拷贝数据到userDTO true忽略大小写
            return userDTO;
        }else{
            //自定义异常
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }

    }
}
