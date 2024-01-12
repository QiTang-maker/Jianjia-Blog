package com.dyj.runner;

import com.dyj.constants.SystemConstants;
import com.dyj.domain.entity.Article;
import com.dyj.mapper.ArticleMapper;
import com.dyj.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        // 查询博客信息 id viewCount
        // 因为Long存储到Redis是1L的格式，无法自增，所以转换为Integer类型
        Map<String, Integer> viewCountMap = articleMapper.selectList(null).stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> {
                    return article.getViewCount().intValue();
                }));
        // 存储到Redis中
        redisCache.setCacheMap(SystemConstants.VIEW_COUNT_CACHE_PREFIX, viewCountMap);
    }
}
