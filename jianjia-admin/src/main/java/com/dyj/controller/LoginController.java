package com.dyj.controller;


import com.dyj.annotation.SystemLog;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.entity.LoginUser;
import com.dyj.domain.entity.Menu;
import com.dyj.domain.entity.User;
import com.dyj.domain.vo.AdminUserInfoVo;
import com.dyj.domain.vo.RoutersVo;
import com.dyj.domain.vo.UserInfoVo;
import com.dyj.enums.AppHttpCodeEnum;
import com.dyj.exception.SystemException;
import com.dyj.service.LoginService;
import com.dyj.service.MenuService;
import com.dyj.service.RoleService;
import com.dyj.utils.BeanCopyUtils;
import com.dyj.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    @SystemLog(BusinessName = "后台登录")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            // 提示 必须要传入用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }

        return loginService.login(user);
    }

    @GetMapping("/getInfo")
    @SystemLog(BusinessName = "查询后台用户角色权限信息")
    public ResponseResult<AdminUserInfoVo> getInfo() {
        // 获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 根据用户Id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        // 根据用户Id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        // 封装数据返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);

        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    @SystemLog(BusinessName = "获取动态路由")
    public ResponseResult<RoutersVo> getRouters() {
        Long userId = SecurityUtils.getUserId();
        // 查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        // 封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    @SystemLog(BusinessName = "后台登出")
    public ResponseResult logout() {
        return loginService.logout();
    }

}
