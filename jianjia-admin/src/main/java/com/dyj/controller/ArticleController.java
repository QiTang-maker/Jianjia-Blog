package com.dyj.controller;

import com.dyj.annotation.SystemLog;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.AddArticleDto;
import com.dyj.domain.vo.ArticleEchoVo;
import com.dyj.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    @SystemLog(BusinessName = "新增文章")
    public ResponseResult add(@RequestBody AddArticleDto articleDto) {
        return articleService.add(articleDto);
    }

    @GetMapping("/list")
    @SystemLog(BusinessName = "分页查询文章列表")
    public ResponseResult list(Integer pageNum, Integer pageSize, String title, String summary) {
        return articleService.listArticlePage(pageNum, pageSize, title, summary);
    }

    @GetMapping("/{id}")
    @SystemLog(BusinessName = "回显文章信息")
    public ResponseResult getArticleEcho(@PathVariable("id") Long id) {
        return articleService.getArticleEcho(id);
    }

    @PutMapping
    @SystemLog(BusinessName = "更新文章信息")
    public ResponseResult updateArticle(@RequestBody ArticleEchoVo articleEchoVo) {
        return articleService.updateArticle(articleEchoVo);
    }

    @DeleteMapping("/{id}")
    @SystemLog(BusinessName = "删除文章")
    public ResponseResult deleteArticle(@PathVariable("id") Long articleId) {
        return articleService.deleteArticle(articleId);
    }

}
