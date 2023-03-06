package com.ypt.springboot.service;

import com.ypt.springboot.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author kkk
 * @since 2022-02-21
 */
public interface IRoleService extends IService<Role> {


    void setRoleMenu(Integer roleId, List<Integer> menuIds);

    List<Integer> getRoleMenu(Integer roleId);

}
