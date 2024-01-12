package com.dyj.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.dyj.annotation.SystemLog;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.AddCategoryDto;
import com.dyj.domain.entity.Category;
import com.dyj.domain.vo.CategoryVo;
import com.dyj.domain.vo.ExcelCategoryVo;
import com.dyj.enums.AppHttpCodeEnum;
import com.dyj.service.CategoryService;
import com.dyj.utils.BeanCopyUtils;
import com.dyj.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    @SystemLog(BusinessName = "查询所有分类")
    public ResponseResult listAllCategory() {
        return categoryService.listAllCategory();
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    @SystemLog(BusinessName = "导出Excel文件")
    public void export(HttpServletResponse response) {
        // 设置下载文件的请求头
        try {
            WebUtils.setDownLoadHeader("分类.xlsx", response);

            // 获取需要导出的数据
            List<Category> categoryList = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryList, ExcelCategoryVo.class);
            // 把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            e.printStackTrace();

            // 如果出现异常 也要响应json数据
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @GetMapping("/list")
    @SystemLog(BusinessName = "分页查询分类列表")
    public ResponseResult listCategory(Integer pageNum, Integer pageSize, String name, String status) {
        return categoryService.listCategory(pageNum, pageSize, name, status);
    }

    @PostMapping
    @SystemLog(BusinessName = "新增分类")
    public ResponseResult addCategory(@RequestBody AddCategoryDto addCategoryDto) {
        return categoryService.addCategory(addCategoryDto);
    }

    @GetMapping("/{id}")
    @SystemLog(BusinessName = "回显分类信息")
    public ResponseResult<CategoryVo> getCategoryEcho(@PathVariable("id") Long categoryId) {
        return categoryService.getCategoryEcho(categoryId);
    }

    @PutMapping
    @SystemLog(BusinessName = "更新分类")
    public ResponseResult updateCategory(@RequestBody CategoryVo categoryVo) {
        return categoryService.updateCategory(categoryVo);
    }

    @DeleteMapping("/{id}")
    @SystemLog(BusinessName = "删除分类")
    public ResponseResult deleteCategory(@PathVariable("id") Long categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

}
