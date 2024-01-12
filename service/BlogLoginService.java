package com.dyj.service;

import com.dyj.domain.ResponseResult;
import com.dyj.domain.entity.User;

public interface BlogLoginService {

    ResponseResult login(User user);

    ResponseResult logout();
}
