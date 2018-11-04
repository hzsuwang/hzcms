package com.hzsuwang.hzcms.domain;

public enum ArticleType {
    INFO(0, "资讯");

    // 构造方法
    private ArticleType(int type, String desc){
        this.desc = desc;
        this.type = type;
    }

    private final String desc;

    private final int    type;

    public String getDesc() {
        return desc;
    }

    public int getType() {
        return type;
    }

    public static ArticleType getArticleType(int type) {
        ArticleType[] enums = ArticleType.values();
        for (ArticleType bannerTypeEnum : enums) {
            if (bannerTypeEnum.getType() == type) {
                return bannerTypeEnum;
            }
        }
        return null;
    }
}
