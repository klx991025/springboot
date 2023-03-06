package com.ypt.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ypt.springboot.common.Constants;
import com.ypt.springboot.controller.dto.UserDTO;
import com.ypt.springboot.entity.LoginLog;
import com.ypt.springboot.entity.Menu;
import com.ypt.springboot.entity.User;
import com.ypt.springboot.exception.ServiceException;
import com.ypt.springboot.mapper.LoginMapper;
import com.ypt.springboot.mapper.RoleMapper;
import com.ypt.springboot.mapper.RoleMenuMapper;
import com.ypt.springboot.mapper.UserMapper;
import com.ypt.springboot.service.IMenuService;
import com.ypt.springboot.service.IUserService;
import com.ypt.springboot.service.LoginService;
import com.ypt.springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kkk
 * @since 2022-10-05
 */
@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, LoginLog> implements LoginService {

}
