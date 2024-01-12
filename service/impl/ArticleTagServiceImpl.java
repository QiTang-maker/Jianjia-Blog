package com.dyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyj.domain.entity.ArticleTag;
import com.dyj.mapper.ArticleTagMapper;
import com.dyj.service.ArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author KevinD
 * @since 2024-01-04 17:04:50
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public void updateArticleTags(Long articleId, List<Long> tags) {
        // 删除原有的文章标签关联数据
        articleTagService.deleteArticleTags(articleId);
        // 添加修改后的文章标签关联数据
        List<ArticleTag> articleTagList = tags.stream()
                .map(tag -> new ArticleTag(articleId, tag))
                .collect(Collectors.toList());
        articleTagService.saveOrUpdateBatch(articleTagList);
    }

    @Override
    public void deleteArticleTags(Long articleId) {
        // 删除对应的的文章标签关联数据
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, articleId);
        articleTagService.remove(queryWrapper);
    }
}
