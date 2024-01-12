package com.dyj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.TagListDto;
import com.dyj.domain.entity.Tag;
import com.dyj.domain.vo.PageVo;


/**
 * 标签(Tag)表服务接口
 *
 * @author KevinD
 * @since 2024-01-02 01:38:28
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult deleteTag(Long tagId);

    ResponseResult getTagDetail(Long tagId);

    ResponseResult updateTag(Tag tag);

    ResponseResult listAllTag();
}
