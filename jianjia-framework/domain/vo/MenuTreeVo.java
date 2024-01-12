package com.dyj.domain.vo;


import com.dyj.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuTreeVo {

    //菜单ID
    private Long id;

    //菜单名称
    private String menuName;
    //父菜单ID
    private Long parentId;

    private List<Menu> children;

}
