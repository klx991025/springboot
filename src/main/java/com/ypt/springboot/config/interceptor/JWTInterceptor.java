package com.ypt.springboot.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ypt.springboot.common.Constants;
import com.ypt.springboot.entity.User;
import com.ypt.springboot.exception.ServiceException;
import com.ypt.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        //如果不是映射方法则直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        //执行认证
        if(StrUtil.isBlank(token)){
            throw new ServiceException(Constants.CODE_401,"无token，重新登陆");
        }
        //获取token中的id
        String userId;
        try{
            userId = JWT.decode(token).getAudience().get(0);
        }catch (JWTDecodeException j){
            throw new ServiceException(Constants.CODE_401,"token验证失败");
        }
        //根据token中的Userid查询数据信息
        User user = userService.getById(userId);
        if(user == null){
            throw new ServiceException(Constants.CODE_401,"用户不存在，重新登录");
        }
        //用户密码加签 验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try{
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            throw new ServiceException(Constants.CODE_401,"token验证失败，重新登录");
        }
        request.setAttribute("userId",user.getId());

        return true;
    }
}
