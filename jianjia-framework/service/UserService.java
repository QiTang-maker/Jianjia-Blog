package com.dyj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.AddUserDto;
import com.dyj.domain.dto.UpdateUserDto;
import com.dyj.domain.entity.User;
import com.dyj.domain.vo.UserEchoVo;
import org.springframework.stereotype.Service;

public interface UserService extends IService<User> {
    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult listUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(AddUserDto addUserDto);

    ResponseResult deleteUser(Long userId);

    ResponseResult<UserEchoVo> listUserEcho(Long userId);

    ResponseResult updateSystemUserInfo(UpdateUserDto updateUserDto);
}
