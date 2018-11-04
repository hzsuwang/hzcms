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
    private String  imgLocal        = "D:/";
    /**
     * 文件服务器域名
     */
    private String  imgServerDomain = "http://192.168.20.74";

}
