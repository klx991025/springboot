package com.ypt.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author kkk
 * @since 2023-03-04
 */
@Getter
@Setter
@TableName("ypt_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * id
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 用户名
     */
      private String username;

      /**
     * 密码
     */
      private String password;

      /**
     * 用户名
     */
      private String nickname;

      /**
     * 电话
     */
      private String phone;

      /**
     * 创建时间
     */
      private Date createTime;

      /**
     * 角色
     */
      private String role;

}
