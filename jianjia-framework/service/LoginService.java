package com.dyj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
