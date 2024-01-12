package com.dyj.job;

import com.dyj.constants.SystemConstants;
import com.dyj.domain.entity.Article;
import com.dyj.service.ArticleService;
import com.dyj.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void updateViewCount() {
        // 获取Redis中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT_CACHE_PREFIX);

        List<Article> articles = viewCountMap.entrySet().stream()
                .map(entry -> {
                    Article article = new Article();
                    article.setId(Long.valueOf(entry.getKey()));
                    article.setViewCount(entry.getValue().longValue());
                    return article;
                }).collect(Collectors.toList());

        // 将浏览量更新到数据库中
        articleService.updateBatchById(articles);

    }
}
