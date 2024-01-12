package com.dyj.service.impl;

import com.dyj.domain.ResponseResult;
import com.dyj.domain.entity.LoginUser;
import com.dyj.domain.entity.User;
import com.dyj.service.LoginService;
import com.dyj.utils.JwtUtil;
import com.dyj.utils.RedisCache;
import com.dyj.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 判断是否认证通过
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 获取UserId生成token
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        // 把用户信息存入Redis
        redisCache.setCacheObject("login:" + userId, loginUser);

        // 把token封装返回
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        // 获取用户Id
        Long userId = SecurityUtils.getUserId();
        // 删除Redis中的缓存
        redisCache.deleteObject("login:" + userId);
        return ResponseResult.okResult();
    }

}
