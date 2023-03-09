//package com.ypt.springboot.config;
//
//import com.ypt.springboot.config.interceptor.JWTInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class InterceptorConfig implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtInterceptor())
//                .addPathPatterns("/**")  //拦截所有请求通过判断token是否合法来决定是否需要登录
//                .excludePathPatterns("/user/login","/user/register","/**/export","/**/import",
//                       "/file/**",
//                        "/test/**");
//    }
//    @Bean
//    public JWTInterceptor jwtInterceptor(){
//        return new JWTInterceptor();
//    }
//
//}
