package com.hzsuwang.hzcms.webx.admin.module.screen;

import com.alibaba.fastjson.JSONObject;
import com.hzsuwang.hzcms.biz.bo.ArticlePoolService;
import com.hzsuwang.hzcms.webx.admin.common.BaseScreen;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 文件导入
 */
public class ImportArticle extends BaseScreen {

    @Autowired
    private ArticlePoolService articlePoolService;

    public JSONObject execute() {
        Map<String, Object> jsonMap = (Map<String, Object>) JSONObject.parse(getContent());
        articlePoolService.importArticle(jsonMap);
        return success();
    }
}
