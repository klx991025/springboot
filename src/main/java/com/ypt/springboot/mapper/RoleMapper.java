package com.ypt.springboot.mapper;

import com.ypt.springboot.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author kkk
 * @since 2022-02-21
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from ypt_role where flag = #{flag}")
    Integer selectByFlag(@Param("flag") String flag);

}
