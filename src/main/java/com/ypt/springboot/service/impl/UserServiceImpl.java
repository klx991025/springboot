package com.ypt.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ypt.springboot.common.Constants;
import com.ypt.springboot.common.RoleEnum;
import com.ypt.springboot.controller.dto.UserDTO;
import com.ypt.springboot.entity.Menu;
import com.ypt.springboot.entity.User;
import com.ypt.springboot.exception.ServiceException;
import com.ypt.springboot.mapper.RoleMapper;
import com.ypt.springboot.mapper.RoleMenuMapper;
import com.ypt.springboot.mapper.UserMapper;
import com.ypt.springboot.service.IMenuService;
import com.ypt.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ypt.springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kkk
 * @since 2023-03-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private IMenuService menuService;


    @Override
    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        //捕获自定义异常
        if (one != null) {
            //拷贝属性                               忽略参数名大小写
            BeanUtil.copyProperties(one, userDTO, true);
            // 设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);
            //获取当前用户的角色
            String role = one.getRole(); // ROLE_ADMIN
            // 设置用户的菜单列表
            List<Menu> roleMenus = getRoleMenus(role);

            userDTO.setMenus(roleMenus);
            userDTO.setUserId(one.getId());
            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public User register(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one == null) {
            one = new User();
            //默认普通用户
            one.setRole(RoleEnum.ROLE_USER.toString());
            BeanUtil.copyProperties(userDTO, one, true);
            // 把 copy完之后的用户对象存储到数据库
            save(one);
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }

    /**
     * 抽取相同代码
     * @param userDTO
     * @return
     */
    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        //捕获系统异常
        try {
            // 从数据库查询用户信息
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }


    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        //根据用户的角色，查出角色表的id
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的id查询所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus(null);
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            //(一级菜单、二级菜单)      //包含当前的一级菜单
            if (menuIds.contains(menu.getId())) {
                //添加一级菜单，包括其下所有子菜单
                roleMenus.add(menu);
            }
            //得到一级菜单下的所有二级菜单
            List<Menu> children = menu.getChildren();
            // removeIf()  移除      不在(一级菜单、二级菜单) 里面的菜单
            //方法用于删除所有满足特定条件的数组元素。
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }

}
