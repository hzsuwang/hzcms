package com.hzsuwang.hzcms.biz.bo.impl;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.hzsuwang.hzcms.common.base.BaseService;
import org.springframework.stereotype.Service;

import com.hzsuwang.hzcms.biz.bo.ArticlePoolService;
import com.hzsuwang.hzcms.domain.ArticlePoolDO;

@Service("articlePoolService")
public class ArticlePoolServiceImpl extends BaseService implements ArticlePoolService {

    @Override
    public ArticlePoolDO getById(long id) {
        return null;
    }

    @Override
    public ArticlePoolDO getByUrl(String url) {
        return null;
    }

    @Override
    public boolean importArticle(Map<String, Object> t) {
        if (t == null || t.size() == 0) {
            return false;
        }
        for (Map.Entry entry : t.entrySet()) {
            logger.info(entry.getKey() + "=" + entry.getKey());
        }
        return true;
    }

    @Override
    public void update(ArticlePoolDO article) {

    }

    @Override
    public void remove(long id) {

    }

    @Override
    public void batchRemove(List<Long> ids) {

    }
}
