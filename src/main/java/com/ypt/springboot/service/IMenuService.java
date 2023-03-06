package com.ypt.springboot.service;

import com.ypt.springboot.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author kkk
 * @since 2022-02-21
 */
public interface IMenuService extends IService<Menu> {
    List<Menu> findMenus(String name);

}
