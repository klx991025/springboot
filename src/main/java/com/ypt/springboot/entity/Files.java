package com.ypt.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("ypt_file")
public class Files {
    private Integer id;
    private Integer userId;
    private String name;
    private String type;
    private String size;
    private String url;
}
