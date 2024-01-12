package com.dyj.controller;


import com.dyj.annotation.SystemLog;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.AddRoleDto;
import com.dyj.domain.dto.UpdateRoleDto;
import com.dyj.domain.entity.Role;
import com.dyj.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.Buffer;
import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    @SystemLog(BusinessName = "分页查询角色列表")
    public ResponseResult listRole(Integer pageNum, Integer pageSize, String roleName, String status) {
        return roleService.listRole(pageNum, pageSize, roleName, status);
    }

    @PutMapping("/changeStatus")
    @SystemLog(BusinessName = "改变角色状态")
    public ResponseResult changeStatus(Long roleId, String status) {
        return roleService.changeStatus(roleId, status);
    }

    @PostMapping
    @SystemLog(BusinessName = "新增角色")
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto) {
        return roleService.addRole(addRoleDto);
    }

    @GetMapping("/{id}")
    @SystemLog(BusinessName = "回显角色信息")
    public ResponseResult getRoleEcho(@PathVariable("id") Long roleId) {
        return roleService.getRoleEcho(roleId);
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    @SystemLog(BusinessName = "加载对应角色菜单列表树")
    public ResponseResult roleMenuTreeSelect(@PathVariable("id") Long roleId) {
        return roleService.roleMenuTreeSelect(roleId);
    }

    @PutMapping
    @SystemLog(BusinessName = "更新角色信息")
    public ResponseResult updateRole(@RequestBody UpdateRoleDto updateRoleDto) {
        return roleService.updateRole(updateRoleDto);
    }

    @DeleteMapping("/{id}")
    @SystemLog(BusinessName = "删除角色")
    public ResponseResult deleteRole(@PathVariable("id") Long roleId) {
        return roleService.deleteRole(roleId);
    }

    @GetMapping("/listAllRole")
    @SystemLog(BusinessName = "查询所有状态正常的角色")
    public ResponseResult<List<Role>> listAllRole() {
        return roleService.listAllRole();
    }

}
