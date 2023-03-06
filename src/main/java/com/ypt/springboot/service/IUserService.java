package com.ypt.springboot.service;

import com.ypt.springboot.controller.dto.UserDTO;
import com.ypt.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kkk
 * @since 2023-03-04
 */
public interface IUserService extends IService<User> {
    User register(UserDTO userDTO);

    UserDTO login(UserDTO userDTO);

}
