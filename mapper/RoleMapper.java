package com.dyj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dyj.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author KevinD
 * @since 2024-01-02 02:52:24
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);
}
