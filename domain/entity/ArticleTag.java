package com.dyj.domain.entity;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 文章标签关联表(ArticleTag)表实体类
 *
 * @author KevinD
 * @since 2024-01-04 17:04:49
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("jj_article_tag")
public class ArticleTag {
    //文章id
    @TableId
    private Long articleId;
    //标签id
    @TableId
    private Long tagId;


}
