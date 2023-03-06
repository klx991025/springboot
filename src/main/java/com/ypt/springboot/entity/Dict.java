package com.ypt.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
//词典
@TableName("ypt_dict")
@Data
public class Dict {

    private String name;
    private String value;
    private String type;

}
