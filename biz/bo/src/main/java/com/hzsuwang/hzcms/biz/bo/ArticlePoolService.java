package com.hzsuwang.hzcms.biz.bo;

import com.hzsuwang.hzcms.domain.ArticlePoolDO;

import java.util.Hashtable;
import java.util.List;

public interface ArticlePoolService {

    /**
     * 根据流水号查找文章
     *
     * @param id 文章流水号
     * @return 文章内容
     */
    public ArticlePoolDO getById(long id);

    /**
     * 导入文章
     *
     * @param t 文章内容
     * @return
     */
    public boolean importArticle(Hashtable t);

    /**
     * 更新的文章到文章池中
     *
     * @param article 文章内容
     */
    public void update(ArticlePoolDO article);

    /**
     * 删除文章池内的文章
     *
     * @param id 文章流水号
     */
    public void remove(long id);

    /**
     * 批量删除文章池内的文章
     *
     * @param ids 文章流水号集合
     */
    public void batchRemove(List<Long> ids);

}
