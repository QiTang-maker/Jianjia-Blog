package com.dyj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyj.domain.entity.RoleMenu;

import java.util.List;


/**
 * 角色和菜单关联表(RoleMenu)表服务接口
 *
 * @author KevinD
 * @since 2024-01-09 00:05:19
 */
public interface RoleMenuService extends IService<RoleMenu> {

    void updateRoleMenu(Long id, List<Long> menuIds);
}
