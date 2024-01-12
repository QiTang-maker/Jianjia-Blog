package com.dyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.TagListDto;
import com.dyj.domain.entity.Tag;
import com.dyj.domain.vo.PageVo;
import com.dyj.domain.vo.TagVo;
import com.dyj.enums.AppHttpCodeEnum;
import com.dyj.exception.SystemException;
import com.dyj.mapper.TagMapper;
import com.dyj.service.TagService;
import com.dyj.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签(Tag)表服务实现类
 *
 * @author KevinD
 * @since 2024-01-02 01:38:30
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        // 分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());

        Page<Tag> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        // 封装数据返回
        List<TagVo> tagVoList = page.getRecords()
                .stream()
                .map(tag -> BeanCopyUtils.copyBean(tag, TagVo.class))
                .collect(Collectors.toList());

        PageVo pageVo = new PageVo(tagVoList, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        if (!StringUtils.hasText(tag.getName())) {
            throw new SystemException(AppHttpCodeEnum.TAG_NAME_NULL);
        }

        save(tag);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long tagId) {
        removeById(tagId);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTagDetail(Long tagId) {
        Tag tag = getById(tagId);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);

        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(Tag tag) {
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        List<Tag> tagList = list();
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tagList, TagVo.class);

        return ResponseResult.okResult(tagVos);
    }
}
