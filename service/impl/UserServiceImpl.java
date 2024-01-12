package com.dyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.AddUserDto;
import com.dyj.domain.dto.UpdateUserDto;
import com.dyj.domain.entity.Role;
import com.dyj.domain.entity.User;
import com.dyj.domain.entity.UserRole;
import com.dyj.domain.vo.*;
import com.dyj.enums.AppHttpCodeEnum;
import com.dyj.exception.SystemException;
import com.dyj.mapper.UserMapper;
import com.dyj.service.RoleService;
import com.dyj.service.UserRoleService;
import com.dyj.service.UserService;
import com.dyj.utils.BeanCopyUtils;
import com.dyj.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult userInfo() {
        // 获取当前用户id
        Long userId = SecurityUtils.getUserId();
        // 根据用户id查询用户信息
        User user = userService.getById(userId);
        // 封装成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult register(User user) {
        // 对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NULL);
        }
        // 对数据进行是否存在（重复）的判断
        if (userNameExit(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExit(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExit(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        // 对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        // 存入数据库
        save(user);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        // 封装查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 可以根据用户名进行模糊搜索
        queryWrapper.like(StringUtils.hasText(userName), User::getUserName, userName);
        // 可以根据手机号进行搜索
        queryWrapper.eq(StringUtils.hasText(phonenumber), User::getPhonenumber, phonenumber);
        // 可以进行状态的查询
        queryWrapper.eq(StringUtils.hasText(status), User::getStatus, status);

        // 分页查询
        Page<User> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        // 封装查询结果并返回
        List<UserVo> userVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserVo.class);
        return ResponseResult.okResult(new PageVo(userVos, page.getTotal()));
    }

    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        // 将密码加密
        String encodePassword = passwordEncoder.encode(addUserDto.getPassword());
        addUserDto.setPassword(encodePassword);
        // 添加用户信息
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        save(user);
        // 添加用户角色关联信息
        userRoleService.addUserRole(user.getId(), addUserDto.getRoleIds());

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUser(Long userId) {
        removeById(userId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<UserEchoVo> listUserEcho(Long userId) {
        // 查询所有的角色信息
        List<Role> roles = roleService.listAllRole().getData();
        List<RoleEchoVo> roleEchoVos = BeanCopyUtils.copyBeanList(roles, RoleEchoVo.class);
        // 查询用户关联的角色信息
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, userId);
        List<UserRole> userRoles = userRoleService.list(queryWrapper);
        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        // 查询用户信息
        User user = getById(userId);
        UserVo userVo = BeanCopyUtils.copyBean(user, UserVo.class);
        // 封装数据并返回
        return ResponseResult.okResult(new UserEchoVo(roleIds, roleEchoVos, userVo));
    }

    @Override
    public ResponseResult updateSystemUserInfo(UpdateUserDto updateUserDto) {
        // 修改用户信息
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        updateById(user);

        // 修改用户关联的角色信息
        userRoleService.updateUserRole(updateUserDto.getId(), updateUserDto.getRoleIds());

        return ResponseResult.okResult();
    }

    private boolean emailExit(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);

        return userService.count(queryWrapper) > 0;
    }

    private boolean nickNameExit(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickName);

        return userService.count(queryWrapper) > 0;
    }

    private boolean userNameExit(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);

        return userService.count(queryWrapper) > 0;
    }
}
