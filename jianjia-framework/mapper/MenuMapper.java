package com.dyj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dyj.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author KevinD
 * @since 2024-01-02 02:44:13
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId();

}
