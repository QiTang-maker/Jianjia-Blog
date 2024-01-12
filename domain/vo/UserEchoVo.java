package com.dyj.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEchoVo {

    // 用户关联的角色信息
    private List<Long> roleIds;
    // 所有角色的信息
    private List<RoleEchoVo> roles;
    // 用户信息
    private UserVo user;

}
