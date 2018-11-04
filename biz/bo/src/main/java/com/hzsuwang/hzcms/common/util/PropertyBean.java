package com.hzsuwang.hzcms.common.util;

import lombok.Data;

@Data
public class PropertyBean implements java.io.Serializable {

    /**
     *
     */
    private boolean production;
    /**
     * 本地文件目录
     */
    private String  imgLocal         = "D:/";
    /**
     * 文件服务器域名
     */
    private String  imgServerDomain  = "http://192.168.20.74";

    private String  aliyunBucketName = "hzcmsfile";                                      // 存储节点名称
    private String  aliyunAccessId   = "LTAIRSxkyvm9oF4o";                               // 存储id
    private String  aliyunAccessKey  = "8xhiDjeQLm1MiwTY3c4gVluxKHNmC7";                 // 存储key
    // oss-cn-hangzhou-internal.aliyuncs.com
    //外网  hzcmsfile.oss-cn-hangzhou.aliyuncs.com
    private String  aliyunOssUrl     = "hzcmsfile.oss-cn-hangzhou-internal.aliyuncs.com"; // 存儲URL

}
