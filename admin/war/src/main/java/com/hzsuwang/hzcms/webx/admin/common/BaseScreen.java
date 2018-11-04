package com.hzsuwang.hzcms.webx.admin.common;

import com.alibaba.fastjson.JSONObject;
import com.hzsuwang.hzcms.webx.admin.common.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BaseScreen {

    protected Logger           logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HttpServletRequest request;

    protected JSONObject success(JSONObject data) {
        JSONObject result = new JSONObject();
        result.put("rc", 1);
        result.put("msg", "成功");
        result.put("data", data);
        return result;
    }

    protected JSONObject success() {
        JSONObject result = new JSONObject();
        result.put("rc", 1);
        result.put("msg", "成功");
        return result;
    }

    public String getContent() {

        StringBuilder buffer = new StringBuilder();
        InputStream is = null;
        try {
            is = request.getInputStream();
            String content = "";
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((content = reader.readLine()) != null) {
                    buffer.append(content);
                }
            }
        } catch (IOException e) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    // do noting
                }
            }
        }

        if (logger.isInfoEnabled()) {
            logger.info(this.getClass().getSimpleName() + " request :" + buffer.toString() + " req_ip=" + IpUtil.getRemoteHost(request));
        }

        return buffer.toString();
    }

    public JSONObject getContentJson() {

        StringBuilder buffer = new StringBuilder();
        InputStream is = null;
        try {
            is = request.getInputStream();
            String content = "";
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((content = reader.readLine()) != null) {
                    buffer.append(content);
                }
            }
        } catch (IOException e) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    // do noting
                }
            }
        }

        if (logger.isInfoEnabled()) {
            logger.info(this.getClass().getSimpleName() + " request :" + buffer.toString() + " req_ip=" + IpUtil.getRemoteHost(request));
        }

        return JSONObject.parseObject(buffer.toString());
    }
}
