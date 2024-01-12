package com.dyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyj.constants.SystemConstants;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.AddRoleDto;
import com.dyj.domain.dto.UpdateRoleDto;
import com.dyj.domain.entity.Role;
import com.dyj.domain.entity.RoleMenu;
import com.dyj.domain.vo.*;
import com.dyj.mapper.RoleMapper;
import com.dyj.service.MenuService;
import com.dyj.service.RoleMenuService;
import com.dyj.service.RoleService;
import com.dyj.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author KevinD
 * @since 2024-01-02 02:52:24
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private MenuService menuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        // 判断是否是管理员 如果是 返回集合中只需要有admin
        if (id.equals(1L)) {
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        // 否则 查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult listRole(Integer pageNum, Integer pageSize, String roleName, String status) {
        // 封装查询条件
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        // 针对角色名称进行模糊查询
        queryWrapper.like(StringUtils.hasText(roleName), Role::getRoleName, roleName);
        // 针对状态进行查询
        queryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        // 按照roleSort进行升序排列
        queryWrapper.orderByAsc(Role::getRoleSort);
        // 分页查询
        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(page.getRecords(), RoleVo.class);

        // 封装返回查询数据
        return ResponseResult.okResult(new PageVo(roleVos, page.getTotal()));
    }

    @Override
    public ResponseResult changeStatus(Long roleId, String status) {
        Role role = getById(roleId);
        role.setStatus(status);
        updateById(role);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        // 添加角色
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);

        // 添加新的角色菜单关系
        List<RoleMenu> roleMenus = addRoleDto.getMenuIds()
                .stream()
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleEcho(Long roleId) {
        // 获取角色信息
        Role role = getById(roleId);
        RoleEchoVo roleEchoVo = BeanCopyUtils.copyBean(role, RoleEchoVo.class);
        return ResponseResult.okResult(roleEchoVo);
    }

    @Override
    public ResponseResult roleMenuTreeSelect(Long roleId) {
        // 获取对应角色的关联菜单信息
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, roleId);
        List<Long> menus = roleMenuService.list(queryWrapper)
                .stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());
        // 获取菜单树
        List<MenuTreeVo> menuTreeVos = menuService.treeSelect().getData();
        // 封装数据并返回
        return ResponseResult.okResult(new RoleMenuVo(menuTreeVos, menus));
    }

    @Override
    public ResponseResult updateRole(UpdateRoleDto updateRoleDto) {
        // 修改角色信息
        Role role = BeanCopyUtils.copyBean(updateRoleDto, Role.class);
        updateById(role);
        // 修改角色关联的菜单信息
        roleMenuService.updateRoleMenu(updateRoleDto.getId(), updateRoleDto.getMenuIds());

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(Long roleId) {
        removeById(roleId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<List<Role>> listAllRole() {
        // 查询所有状态正常的角色
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> roles = list(queryWrapper);

        return ResponseResult.okResult(roles);
    }
}
