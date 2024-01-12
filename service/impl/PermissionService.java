package com.dyj.service.impl;

import com.dyj.mapper.MenuMapper;
import com.dyj.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {

    /**
     * 判断当前用户是否具有permission
     *
     * @param permission 要判断的权限
     * @return
     */
    public boolean hasPermission(String permission) {
        // 如果是超级管理员 直接返回true
        if (SecurityUtils.isAdmin()) {
            return true;
        }
        // 否则 判断当前用户是否具有对应权限permission
        List<String> perms = SecurityUtils.getLoginUser().getPermissions();
        return perms.contains(permission);
    }
}
