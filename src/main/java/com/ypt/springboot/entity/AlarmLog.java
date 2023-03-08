package com.ypt.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName("ypt_alarm_log")
public class AlarmLog {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String level;
    private String status;
    private String updateBy;
    private Date date;
    private Date updateTime;
}
