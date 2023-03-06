package com.ypt.springboot.controller.dto;


import com.ypt.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

//接受前端请求参数
@Data
public class UserDTO {
    private String username;
    private String password;
    private String nickname;
    private String token;
    private String role;
    private List<Menu> menus;
    private Integer userId;
}
