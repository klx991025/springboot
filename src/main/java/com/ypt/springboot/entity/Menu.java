package com.ypt.springboot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author 午子牛
 * @since 2022-02-21
 */
@Getter
@Setter
@TableName("ypt_menu")
@ApiModel(value = "Menu对象", description = "菜单表")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("路径")
    private String path;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("描述")
    private String description;

    //数据不存在
    @TableField(exist = false)

    @ApiModelProperty("孩子")
    private List<Menu> children;

    @ApiModelProperty("父id")
    private Integer pid;

    @ApiModelProperty("路由路径")
    private String pagePath;

    @ApiModelProperty("数量排序")
    private String sortNum;



}
