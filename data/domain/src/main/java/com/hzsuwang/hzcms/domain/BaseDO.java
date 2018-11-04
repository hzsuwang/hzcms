package com.hzsuwang.hzcms.domain;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;

import java.util.Date;

@Data
public class BaseDO implements java.io.Serializable {

    @Id
    @Comment("序号")
    private long id;

    @Column("create_time")
    @Comment("创建时间")
    private Date createTime;

    @Column("edit_time")
    @Comment("编辑时间")
    private Date editTime;

    @Column("deleted")
    @Comment("删除状态")
    private int  deleted;
}
