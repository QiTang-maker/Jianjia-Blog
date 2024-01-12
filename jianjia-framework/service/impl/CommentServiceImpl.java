package com.dyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyj.constants.SystemConstants;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.entity.Comment;
import com.dyj.domain.vo.CommentVo;
import com.dyj.domain.vo.PageVo;
import com.dyj.enums.AppHttpCodeEnum;
import com.dyj.exception.SystemException;
import com.dyj.mapper.CommentMapper;
import com.dyj.service.CommentService;
import com.dyj.service.UserService;
import com.dyj.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author KevinD
 * @since 2023-12-19 21:46:39
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, int pageNum, int pageSize) {
        // 查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        // 文章Id
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType), Comment::getArticleId, articleId);
        // 评论类型
        queryWrapper.eq(Comment::getType, commentType);
        // 根评论 rootId为-1
        queryWrapper.eq(Comment::getRootId, SystemConstants.ROOT_COMMENT_ID);
        // 分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<CommentVo> commentVos = toCommentVoList(page.getRecords());

        // 查询所有根评论对应的子评论集合，并且赋值给对应的属性
        commentVos = commentVos.stream()
                // 查询对应的子评论并赋值
                .map(commentVo -> commentVo.setChileren(getChildren(commentVo.getId())))
                .collect(Collectors.toList());

        return ResponseResult.okResult(new PageVo(commentVos, page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        // 评论内容不能为空
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTEXT_NULL);
        }

        save(comment); // 其余公共部分字段由mybatisplus自动填充2

        return ResponseResult.okResult();
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     *
     * @param id 根评论的id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        return toCommentVoList(list(queryWrapper));
    }

    private List<CommentVo> toCommentVoList(List<Comment> commentList) {
        // 封装为commentVo
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(commentList, CommentVo.class);
        // 设置commentVo中的toCommentUserName和username（查询的是昵称，即NickName）
        // toCommentId不为SystemConstants.ROOT_COMMENT_ID才查询
        commentVos = commentVos.stream()
                .map(commentVo -> commentVo.getToCommentUserId() != SystemConstants.ROOT_COMMENT_ID
                        ? commentVo.setToCommentUserName(userService.getById(commentVo.getToCommentUserId()).getNickName())
                        : commentVo)
                .map(commentVo -> commentVo.setUsername(userService.getById(commentVo.getCreateBy()).getNickName()))
                .collect(Collectors.toList());

        return commentVos;
    }
}
