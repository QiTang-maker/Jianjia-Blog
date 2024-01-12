package com.dyj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author KevinD
 * @since 2023-12-19 21:46:39
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, int pageNum, int pageSize);

    ResponseResult addComment(Comment comment);
}
