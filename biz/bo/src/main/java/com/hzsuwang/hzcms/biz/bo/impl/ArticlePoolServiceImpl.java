package com.hzsuwang.hzcms.biz.bo.impl;

import java.util.Hashtable;
import java.util.List;

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
    public boolean importArticle(Hashtable t) {
        return false;
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
