package com.biaoge.vuespringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.biaoge.vuespringboot.controller.dto.UserDTO;
import com.biaoge.vuespringboot.entity.User;

public interface IUserService extends IService<User> {
    UserDTO login(UserDTO userDTO);
}
