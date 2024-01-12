package com.dyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyj.domain.entity.UserRole;
import com.dyj.mapper.UserRoleMapper;
import com.dyj.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author KevinD
 * @since 2024-01-09 03:44:10
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public void addUserRole(Long userId, List<Long> roleIds) {
        List<UserRole> userRoles = roleIds.stream()
                .map(r -> new UserRole(userId, r))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
    }

    @Override
    public void updateUserRole(Long userId, List<Long> roleIds) {
        // 删除原有的用户角色关联关系
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, userId);
        remove(queryWrapper);
        // 添加新的用户角色关联关系
        addUserRole(userId, roleIds);
    }
}
