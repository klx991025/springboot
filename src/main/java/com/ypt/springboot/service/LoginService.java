package com.ypt.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ypt.springboot.controller.dto.UserDTO;
import com.ypt.springboot.entity.LoginLog;
import com.ypt.springboot.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kkk
 * @since 2022-10-05
 */
public interface LoginService extends IService<LoginLog> {
}
