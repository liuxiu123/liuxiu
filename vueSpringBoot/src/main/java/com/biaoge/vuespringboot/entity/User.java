package com.biaoge.vuespringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@TableName(value="sys_user")
@ToString
public class User {
    @TableId(type= IdType.AUTO)  //指定主键
    private Integer id;
    private String username;
    @JsonIgnore //忽略某个字段
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String address;
    private String avatarUrl;
    private Date createTime;
}
