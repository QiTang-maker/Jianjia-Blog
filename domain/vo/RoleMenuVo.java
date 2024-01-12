package com.dyj.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuVo {

    //菜单树
    private List<MenuTreeVo> menus;
    //角色关联的菜单Id
    private List<Long> checkedKeys;

}
