package com.dyj.controller;


import com.dyj.annotation.SystemLog;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.AddUserDto;
import com.dyj.domain.dto.UpdateUserDto;
import com.dyj.domain.vo.UserEchoVo;
import com.dyj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    @SystemLog(BusinessName = "展示用户列表")
    public ResponseResult listUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        return userService.listUser(pageNum, pageSize, userName, phonenumber, status);
    }

    @PostMapping
    @SystemLog(BusinessName = "新增用户")
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto) {
        return userService.addUser(addUserDto);
    }

    @DeleteMapping("/{id}")
    @SystemLog(BusinessName = "删除用户")
    public ResponseResult deleteUser(@PathVariable("id") Long userId) {
        return userService.deleteUser(userId);
    }

    @GetMapping("/{id}")
    @SystemLog(BusinessName = "查询用户回显信息")
    public ResponseResult<UserEchoVo> listUserEcho(@PathVariable("id") Long userId) {
        return userService.listUserEcho(userId);
    }

    @PutMapping
    @SystemLog(BusinessName = "修改用户信息")
    public ResponseResult updateSystemUserInfo(@RequestBody UpdateUserDto updateUserDto) {
        return userService.updateSystemUserInfo(updateUserDto);
    }

}
