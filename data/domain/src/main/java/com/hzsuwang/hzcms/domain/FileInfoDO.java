package com.hzsuwang.hzcms.domain;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("cms_file_info")
public class FileInfoDO extends BaseDO {

    @Column("file_name")
    @Comment("文件名称")
    private String fileName;

    @Column("file_md5")
    @Comment("文件的md5值")
    private String fileMd5;

    @Column("file_path")
    @Comment("服务器地扯")
    private String filePath;

    @Column("file_length")
    @Comment("文件长度")
    private long   fileLength;

    @Column("file_ext")
    @Comment("文件扩展名")
    private String fileExt;
}
