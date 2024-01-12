package com.dyj.controller;

import com.dyj.annotation.SystemLog;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.entity.Menu;
import com.dyj.domain.vo.MenuTreeVo;
import com.dyj.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    @SystemLog(BusinessName = "菜单列表查询")
    public ResponseResult listMenu(String status, String menuName) {
        return menuService.listMenu(status, menuName);
    }

    @PostMapping
    @SystemLog(BusinessName = "新增菜单")
    public ResponseResult addMenu(Menu menu) {
        return menuService.addMenu(menu);
    }

    @GetMapping("/{id}")
    @SystemLog(BusinessName = "菜单信息查询")
    public ResponseResult menuDetail(@PathVariable("id") Long menuId) {
        return menuService.menuDetail(menuId);
    }

    @PutMapping
    @SystemLog(BusinessName = "更新菜单信息")
    public ResponseResult updateMenu(Menu menu) {
        return menuService.updateMenu(menu);
    }

    @DeleteMapping("/{menuId}")
    @SystemLog(BusinessName = "删除菜单")
    public ResponseResult deleteMenu(@PathVariable("menuId") Long menuId) {
        return menuService.deleteMenu(menuId);
    }

    @GetMapping("/treeselect")
    @SystemLog(BusinessName = "查询菜单树")
    public ResponseResult<List<MenuTreeVo>> treeSelect() {
        return menuService.treeSelect();
    }

}
