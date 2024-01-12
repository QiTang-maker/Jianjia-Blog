package com.dyj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.entity.Menu;
import com.dyj.domain.vo.MenuTreeVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author KevinD
 * @since 2024-01-02 02:44:26
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult listMenu(String status, String menuName);

    ResponseResult addMenu(Menu menu);

    ResponseResult updateMenu(Menu menu);

    ResponseResult menuDetail(Long menuId);

    ResponseResult deleteMenu(Long menuId);

    ResponseResult<List<MenuTreeVo>> treeSelect();
}
