package com.dyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyj.constants.SystemConstants;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.AddArticleDto;
import com.dyj.domain.entity.Article;
import com.dyj.domain.entity.ArticleTag;
import com.dyj.domain.entity.Category;
import com.dyj.domain.entity.Tag;
import com.dyj.domain.vo.*;
import com.dyj.mapper.ArticleMapper;
import com.dyj.service.ArticleService;
import com.dyj.service.ArticleTagService;
import com.dyj.service.CategoryService;
import com.dyj.utils.BeanCopyUtils;
import com.dyj.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author kevinD
 * @since 2023-11-29 14:40:48
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticleList() {
        // 查询热门文章，封装成ResponseResult返回

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 封装查询条件
        // 必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 按照浏览量进行降序排序
        queryWrapper.orderByDesc(Article::getViewCount);
        // 最多查询10条数据
        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);

        // 从Redis获取浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT_CACHE_PREFIX);

        List<Article> articles = page.getRecords().stream()
                .map(o -> o.setViewCount(viewCountMap.get(o.getId().toString()).longValue()))
                .collect(Collectors.toList());

        // 封装返回结果
        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时和传入的相同
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);

        // 状态是正式发布的
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);

        // 对isTop进行降序
        queryWrapper.orderByDesc(Article::getIsTop);

        // 分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        // 从Redis获取浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT_CACHE_PREFIX);

        // 查询分类名称
        List<Article> records = page.getRecords();
        records.stream()
                .map(o -> o.setCategoryName(categoryService.getById(o.getCategoryId()).getName()))
                .map(o -> o.setViewCount(viewCountMap.get(o.getId().toString()).longValue()))
                .collect(Collectors.toList());

        // 封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        // 封装为页查询结果
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        // 根据id查询文章
        Article article = getById(id);

        // 从Redis获取浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT_CACHE_PREFIX);
        article.setViewCount(viewCountMap.get(id.toString()).longValue());

        // 转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);

        // 根据分类id查询分类名
        Category category = categoryService.getById(articleDetailVo.getCategoryId());
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }

        // 封装相应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        // 更新Redis中对应 id的浏览量
        redisCache.increaseCacheMapValue(SystemConstants.VIEW_COUNT_CACHE_PREFIX, id.toString(), 1);

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        // 添加博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article); // MyBatisPlus会把插入数据的Id赋值

        List<ArticleTag> articleTagList = articleDto.getTags()
                .stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        // 添加 博客和标签的关联
        articleTagService.saveBatch(articleTagList);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listArticlePage(Integer pageNum, Integer pageSize, String title, String summary) {
        // 封装查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 对title和summary进行模糊查询
        queryWrapper.like(StringUtils.hasText(title), Article::getTitle, title);
        queryWrapper.like(StringUtils.hasText(summary), Article::getSummary, summary);

        // 从Redis获取浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT_CACHE_PREFIX);

        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        page.getRecords()
                .stream()
                .map(article -> article.setViewCount(viewCountMap.get(article.getId().toString()).longValue()))
                .collect(Collectors.toList());

        // 封装并返回查询分页结果
        return ResponseResult.okResult(new PageVo(page.getRecords(), page.getTotal()));
    }

    @Override
    public ResponseResult getArticleEcho(Long id) {
        // 查询文章信息
        Article article = getById(id);
        // 从Redis获取浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT_CACHE_PREFIX);
        article.setViewCount(viewCountMap.get(article.getId().toString()).longValue());

        // 获取对应文章的标签信息
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId, id);
        List<ArticleTag> articleTags = articleTagService.list(queryWrapper);
        List<Long> tags = articleTags.stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toList());
        // 设置tags信息
        ArticleEchoVo articleEchoVo = BeanCopyUtils.copyBean(article, ArticleEchoVo.class);
        articleEchoVo.setTags(tags);

        // 封装返回数据
        return ResponseResult.okResult(articleEchoVo);
    }

    @Override
    public ResponseResult updateArticle(ArticleEchoVo articleEchoVo) {
        // 更新article表中的文章数据
        Article article = BeanCopyUtils.copyBean(articleEchoVo, Article.class);
        updateById(article);

        // 更新article_tag表中对应文章关联的标签数据
        articleTagService.updateArticleTags(articleEchoVo.getId(), articleEchoVo.getTags());

        // 返回响应数据
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(Long articleId) {
        // 删除article表中的文章信息
        removeById(articleId);

        // 删除article_tag表中的文章标签关联信息
        articleTagService.deleteArticleTags(articleId);

        // 返回响应信息
        return ResponseResult.okResult();
    }
}
