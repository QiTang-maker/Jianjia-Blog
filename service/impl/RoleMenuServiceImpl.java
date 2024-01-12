package com.dyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyj.domain.entity.RoleMenu;
import com.dyj.mapper.RoleMenuMapper;
import com.dyj.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author KevinD
 * @since 2024-01-09 00:05:19
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public void updateRoleMenu(Long roleId, List<Long> menuIds) {
        // 查找角色所有原来关联的菜单信息并删除
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, roleId);
        remove(queryWrapper);
        // 添加新的菜单信息
        List<RoleMenu> roleMenuList = menuIds.stream()
                .map(m -> new RoleMenu(roleId, m))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }
}
