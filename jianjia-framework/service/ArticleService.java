package com.dyj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.AddArticleDto;
import com.dyj.domain.entity.Article;
import com.dyj.domain.vo.ArticleEchoVo;


/**
 * 文章表(Article)表服务接口
 *
 * @author kevinD
 * @since 2023-11-29 14:39:11
 */
public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto articleDto);

    ResponseResult listArticlePage(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult getArticleEcho(Long id);

    ResponseResult updateArticle(ArticleEchoVo articleEchoVo);

    ResponseResult deleteArticle(Long articleId);
}
