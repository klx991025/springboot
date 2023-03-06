package com.ypt.springboot.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("ypt_role_menu")
@Data
public class RoleMenu {

    private Integer roleId;
    private Integer menuId;

}

