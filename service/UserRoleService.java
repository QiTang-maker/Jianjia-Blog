package com.dyj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyj.domain.entity.UserRole;

import java.util.List;


/**
 * 用户和角色关联表(UserRole)表服务接口
 *
 * @author KevinD
 * @since 2024-01-09 03:44:10
 */
public interface UserRoleService extends IService<UserRole> {

    void addUserRole(Long id, List<Long> roleIds);

    void updateUserRole(Long userId, List<Long> roleIds);
}
