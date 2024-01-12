package com.dyj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.AddCategoryDto;
import com.dyj.domain.entity.Category;
import com.dyj.domain.vo.CategoryVo;


/**
 * 分类表(Category)表服务接口
 *
 * @author KevinD
 * @since 2023-12-04 18:49:10
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult listCategory(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(AddCategoryDto addCategoryDto);

    ResponseResult<CategoryVo> getCategoryEcho(Long categoryId);

    ResponseResult updateCategory(CategoryVo categoryVo);

    ResponseResult deleteCategory(Long categoryId);
}
