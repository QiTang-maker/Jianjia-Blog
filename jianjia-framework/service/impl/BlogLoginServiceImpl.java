package com.dyj.service.impl;

import com.dyj.domain.ResponseResult;
import com.dyj.domain.entity.LoginUser;
import com.dyj.domain.entity.User;
import com.dyj.domain.vo.BlogUserLoginVo;
import com.dyj.domain.vo.UserInfoVo;
import com.dyj.service.BlogLoginService;
import com.dyj.utils.BeanCopyUtils;
import com.dyj.utils.JwtUtil;
import com.dyj.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("blogLoginService")
public class BlogLoginServiceImpl implements BlogLoginService {

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
        redisCache.setCacheObject("bloglogin:" + userId, loginUser);

        // 把token和UserInfo封装返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult logout() {
        // 获取token 解析获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long id = loginUser.getUser().getId();
        // 删除Redis中的用户信息
        redisCache.deleteObject("bloglogin:" + id);
        return ResponseResult.okResult();
    }
}
