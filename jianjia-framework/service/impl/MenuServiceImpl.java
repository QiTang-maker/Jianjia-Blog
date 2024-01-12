package com.dyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyj.constants.SystemConstants;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.entity.Menu;
import com.dyj.domain.entity.RoleMenu;
import com.dyj.domain.vo.MenuDetailVo;
import com.dyj.domain.vo.MenuTreeVo;
import com.dyj.domain.vo.MenuVo;
import com.dyj.enums.AppHttpCodeEnum;
import com.dyj.mapper.MenuMapper;
import com.dyj.service.MenuService;
import com.dyj.service.RoleMenuService;
import com.dyj.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author KevinD
 * @since 2024-01-02 02:44:27
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        // 如果是管理员，返回所有的权限
        if (id.equals(1L)) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menuList = list(queryWrapper);
            List<String> perms = menuList.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }

        // 否则返回其所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;

        // 判断是否是管理员
        if (userId.equals(1L)) {
            // 如果是 返回所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        } else {
            // 否则 返回当前用户所具有的的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId();
        }

        // 构建tree
        // 先找出第一层的菜单 然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = buildMenuTree(menus, SystemConstants.TOP_MENU);

        return menuTree;
    }

    @Override
    public ResponseResult listMenu(String status, String menuName) {
        // 封装查询条件
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        // 可以针对菜单的状态进行查询
        queryWrapper.eq(StringUtils.hasText(status), Menu::getStatus, status);
        // 可以针对菜单名进行模糊查询
        queryWrapper.like(StringUtils.hasText(menuName), Menu::getMenuName, menuName);
        // 按照父菜单id和orderNum进行升序排序
        queryWrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        // 封装返回查询结果
        List<Menu> menuList = list(queryWrapper);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menuList, MenuVo.class);

        return ResponseResult.okResult(menuVos);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        // 如果将父菜单设置为当前菜单，修改失败并返回对应信息
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SAME_PARENT_MENU);
        }
        // 修改菜单信息
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult menuDetail(Long menuId) {
        // 查询对应菜单信息
        Menu menu = getById(menuId);
        // 封装返回查询数据
        MenuDetailVo menuDetailVo = BeanCopyUtils.copyBean(menu, MenuDetailVo.class);
        return ResponseResult.okResult(menuDetailVo);
    }

    @Override
    public ResponseResult deleteMenu(Long menuId) {
        // 如果该菜单有子菜单，删除失败
        List<Long> parentIds = list().stream()
                .map(Menu::getParentId)
                .distinct()
                .collect(Collectors.toList());
        if (parentIds.contains(menuId)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CHILDREN_MENU_EXIT);
        }
        // 删除菜单
        removeById(menuId);
        // 封装返回响应结果
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<List<MenuTreeVo>> treeSelect() {
        // 获取菜单树
        List<Menu> menus = selectMenuTree();
        // 封装返回响应数据
        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(menus, MenuTreeVo.class);
        return ResponseResult.okResult(menuTreeVos);
    }

    private List<Menu> selectMenuTree() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Menu::getId);
        // 查询所有菜单
        List<Menu> menuList = list(queryWrapper);
        // 生成菜单树
        List<Menu> menuTree = buildMenuTree(menuList, SystemConstants.TOP_MENU);
        return menuTree;
    }

    private List<Menu> buildMenuTree(List<Menu> menus, long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChilden(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取传入参数的子Menu集合
     *
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChilden(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                // 递归调用 stream流为空时停止调用
                .map(m -> m.setChildren(getChilden(m, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
