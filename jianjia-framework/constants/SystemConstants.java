package com.dyj.constants;

public class SystemConstants {
    /**
     * 文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     * 文章是正常发布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    /**
     * 分类是正常状态
     */
    public static final String STATUS_NORMAL = "0";
    /**
     * 分类是禁用状态
     */
    public  static  final String STATUS_DISABLED = "1";
    /**
     * 审核通过的友链
     */
    public static final int LINK_APPROVED = 0;
    /**
     * 审核未通过的友链
     */
    public static final int LINK_UNAPPROVED = 1;
    /**
     * 未审核的友链
     */
    public static final int LINK_UNAUDITED = 2;
    /**
     * 根评论ID
     */
    public static final int ROOT_COMMENT_ID = -1;
    /**
     * 评论类型：文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * 评论类型：友链评论
     */
    public static final String LINK_COMMENT = "1";
    /**
     * 性别：男
     */
    public static final String SEX_MAN = "0";
    /**
     * 性别：女
     */
    public static final String SEX_WOMAN = "1";
    /**
     * 性别：未知
     */
    public static final String SEX_UNKNOWN = "2";
    /**
     * 浏览量缓存前缀
     */
    public static final String VIEW_COUNT_CACHE_PREFIX = "article:viewCount";
    /**
     * 菜单类型为目录
     */
    public static final String CATEGORY = "M";
    /**
     * 菜单类型为菜单
     */
    public static final String MENU = "C";
    /**
     * 菜单类型为按钮
     */
    public static final String BUTTON = "F";
    /**
     * 前台正常用户
     */
    public static final String NORMAL_USER = "0";
    /**
     * 后台用户
     */
    public static final String ADMIN = "1";
    /**
     * 顶层菜单的父菜单ID
     */
    public static final Long TOP_MENU = 0L;
}