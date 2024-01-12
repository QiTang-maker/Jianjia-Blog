package com.dyj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.AddRoleDto;
import com.dyj.domain.dto.UpdateRoleDto;
import com.dyj.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author KevinD
 * @since 2024-01-02 02:52:24
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult listRole(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeStatus(Long roleId, String status);

    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult getRoleEcho(Long roleId);

    ResponseResult roleMenuTreeSelect(Long roleId);

    ResponseResult updateRole(UpdateRoleDto updateRoleDto);

    ResponseResult deleteRole(Long roleId);

    ResponseResult<List<Role>> listAllRole();
}
