package com.dyj.controller;

import com.dyj.annotation.SystemLog;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.TagListDto;
import com.dyj.domain.entity.Tag;
import com.dyj.domain.vo.PageVo;
import com.dyj.service.TagService;
import com.dyj.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    @SystemLog(BusinessName = "分页查询标签")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    @PostMapping
    @SystemLog(BusinessName = "添加标签")
    public ResponseResult addTag(@RequestBody TagListDto tagListDto) {
        Tag tag = BeanCopyUtils.copyBean(tagListDto, Tag.class);
        return tagService.addTag(tag);
    }

    @DeleteMapping("/{id}")
    @SystemLog(BusinessName = "删除标签")
    public ResponseResult deleteTag(@PathVariable("id") Long tagId) {
        return tagService.deleteTag(tagId);
    }

    @GetMapping("/{id}")
    @SystemLog(BusinessName = "获取标签信息")
    public ResponseResult getTagDetail(@PathVariable("id") Long tagId) {
        return tagService.getTagDetail(tagId);
    }

    @PutMapping
    @SystemLog(BusinessName = "修改标签信息")
    public ResponseResult updateTag(@RequestBody Tag tag) {
        return tagService.updateTag(tag);
    }

    @GetMapping("/listAllTag")
    @SystemLog(BusinessName = "查询所有标签")
    public ResponseResult listAllTag() {
        return tagService.listAllTag();
    }

}
