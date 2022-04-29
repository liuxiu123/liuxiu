package com.biaoge.vuespringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biaoge.vuespringboot.entity.User;
import org.apache.ibatis.annotations.*;
import org.mybatis.spring.annotation.MapperScan;


import java.util.List;

//@Mapper
public interface UserMapper extends BaseMapper<User> {
//    @Select("select * from sys_user")
//    List<User> findAll();
//
//    @Insert("insert into sys_user(username,password,nickname,email,phone,address) " +
//            "values (#{username},#{password},#{nickname},#{email},#{phone},#{address})")
//    int insert(User user);
//
//    int update(User user);
//
//    //删除
//    @Delete("delete from sys_user where id=#{id}")
//    int deleteById(Integer id);
//
//
//    @Select("select  count(*) from sys_user where username like concat('%',#{arg0},'%') and email like concat('%',#{arg1},'%')" +
//            " and address like concat('%',#{arg2},'%')")
//    Integer selectTotal(String username,String email,String address);
//
//    @Select("select * from sys_user where username like concat('%',#{arg2},'%') and email like concat('%',#{arg3},'%') and " +
//            "address like concat('%',#{arg4},'%')  limit #{arg0},#{arg1}")
//    //@Select("select * from sys_user limit #{pageNum},#{pageSize}") '?为什么不能这样写，靠
//    List<User> selectPage(Integer pageNum, Integer pageSize,String username,String email,String address);
}
