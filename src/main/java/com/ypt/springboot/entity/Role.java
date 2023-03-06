package com.ypt.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author kk
 * @since 2022-02-21
 */
@Getter
@Setter
@TableName("ypt_role")
@ApiModel(value = "Role对象", description = "角色表")
public class Role implements Serializable {

    //private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    private String name;


    @ApiModelProperty("唯一标识")
    private String flag;


    @ApiModelProperty("描述")
    private String description;


}
