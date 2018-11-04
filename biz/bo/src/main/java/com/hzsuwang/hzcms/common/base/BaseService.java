package com.hzsuwang.hzcms.common.base;

import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected Dao dao;
}
