package com.hzsuwang.hzcms.webx.admin.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class RequestUtil {

    private static Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    public static String getContent(HttpServletRequest request) {
        StringBuffer buffer = new StringBuffer();
        InputStream is = null;
        try {
            is = request.getInputStream();
            String content = "";

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((content = reader.readLine()) != null) {
                buffer.append(content);
            }

        } catch (IOException e) {
            logger.debug("request.getInputStream failed :", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    // do noting
                }
            }
        }

        return buffer.toString();
    }

    public static JSONObject getJSONContent(HttpServletRequest request) {
        String content = getContent(request);
        JSONObject json = new JSONObject();
        if (content != null && !"".equals(content)) {
            try {
                json = JSONObject.parseObject(content);
            } catch (Exception e) {
                logger.error("parse json error:", e);
            }

        }
        return json;
    }

    public static String sendServiceResult(String url, Map<String, Object> entity) {
        String result = "";
        try {
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put("Content-Type", "application/json;charset=utf-8");
            HttpClient httpclient = new DefaultHttpClient();
            String uri = url;
            HttpPost httppost = new HttpPost(uri);
            if (headerMap != null && headerMap.keySet() != null) {
                Set<String> keys = headerMap.keySet();
                for (String key : keys) {
                    httppost.addHeader(key, headerMap.get(key));

                }
                // httppost.addHeader("Cookie", "JSESSIONID=124_-88696270569_5");
            }
            JSONObject date = new JSONObject();
            if (entity != null && entity.keySet() != null) {
                Set<String> keys = entity.keySet();
                for (String key : keys) {
                    date.put(key, entity.get(key));
                }
            }
            httppost.setEntity(new StringEntity(date.toString(), "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            // 检验状态码，如果成功接收数据
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                return EntityUtils.toString(response.getEntity());// 返回json格式：
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
