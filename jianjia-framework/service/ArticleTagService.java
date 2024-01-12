package com.dyj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyj.domain.entity.ArticleTag;

import java.util.List;


/**
 * 文章标签关联表(ArticleTag)表服务接口
 *
 * @author KevinD
 * @since 2024-01-04 17:04:50
 */
public interface ArticleTagService extends IService<ArticleTag> {

    void updateArticleTags(Long articleId, List<Long> tags);

    void deleteArticleTags(Long articleId);
}
