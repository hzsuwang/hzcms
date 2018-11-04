package com.hzsuwang.hzcms.domain;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("cms_article_pool")
public class ArticlePoolDO extends BaseDO {

    @Column("title")
    @Comment("标题")
    private String title;

    @Column("content")
    @Comment("内容")
    private String content;

    @Column("tag")
    @Comment("标签")
    private String tag;

    @Column("source_url")
    @Comment("来源url")
    private String sourceUrl;

    @Column("source_code")
    @Comment("来源code")
    private int    sourceCode;

    @Column("author")
    @Comment("作者信息")
    private String author;

    @Column("author_url")
    @Comment("作者URl")
    private String authorUrl;

    @Column("catalog")
    @Comment("分类")
    private String catalog;

    @Column("source")
    @Comment("来源")
    private String source;

    @Column("article_type")
    @Comment("文章类型")
    private int    articleType;

    @Column("priority")
    @Comment("文章权重、优先级")
    private int    priority;

    @Column("admin_id")
    @Comment("后台处理人")
    private long   adminId;

    @Column("status")
    @Comment("文章处理状态")
    private int    status;
}
