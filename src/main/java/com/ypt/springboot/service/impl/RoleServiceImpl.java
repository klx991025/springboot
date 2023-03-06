package com.ypt.springboot.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ypt.springboot.entity.Menu;
import com.ypt.springboot.entity.Role;
import com.ypt.springboot.entity.RoleMenu;
import com.ypt.springboot.mapper.RoleMapper;
import com.ypt.springboot.mapper.RoleMenuMapper;
import com.ypt.springboot.service.IMenuService;
import com.ypt.springboot.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author kkk
 * @since 2022-02-21
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    /**
     * 为角色分配菜单
     * 先删后增
     * @param roleId
     * @param menuIds
     */
    //注解回滚
    @Transactional
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {

        //根据id删除菜单
        roleMenuMapper.delete(new QueryWrapper<RoleMenu>().eq("role_id", roleId));



        // 再把前端传过来的菜单id数组绑定到当前的这个角色id上去
        //    赋值list，不能在循环的时候动态的添加元素
        List<Integer> menuIdsCopy = CollUtil.newArrayList(menuIds);
        for (Integer menuId : menuIds) {
            Menu menu = menuService.getById(menuId);
            // 二级菜单 并且传过来的menuId数组里面没有它的父级id
            if (menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())) {
                // 那么我们就得补上这个父级id
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menu.getPid());
                roleMenuMapper.insert(roleMenu);
                //根据复制的元素，判断是否重复添加父级
                menuIdsCopy.add(menu.getPid());
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    /**
     * 根据角色id查询菜单idl列表
     * @param roleId
     * @return
     */
    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }


}
