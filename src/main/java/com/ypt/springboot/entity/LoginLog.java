package com.ypt.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName("ypt_login_log")
public class LoginLog {
    private Integer id;
    private Integer userId;
    private String address;
    private String ip;
    private String web;
    private String os;
    private Date date;
//    @TableField(exist = false)
    private String username;
}
