package com.hzsuwang.hzcms.webx.admin.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {

    private static final String APPLICATION_JSON       = "application/json";
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    private static Logger       logger                 = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String host) {
        String result = "";
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // 设置通用的请求属性
            connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
            connection.setRequestProperty("Host", host);
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");

            // 建立实际的连接
            connection.connect();
            // // 获取所有响应头字段
            // Map<String, List<String>> map = connection.getHeaderFields();
            // // 遍历所有的响应头字段
            // for (String key : map.keySet()) {
            // System.out.println(key + "--->" + map.get(key));
            // }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static String sendGet(String url, String param) {
        String result = "";
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // // 获取所有响应头字段
            // Map<String, List<String>> map = connection.getHeaderFields();
            // // 遍历所有的响应头字段
            // for (String key : map.keySet()) {
            // System.out.println(key + "--->" + map.get(key));
            // }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            logger.error("baidu ip gpsinfo error " + e);
        }
        return result;
    }

    public static String httpPostWithJSON(String url, String json) throws Exception {
        logger.info("robot post url=" + url + " json=" + json);
        // 将JSON进行UTF-8编码,以便传输中文
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
        StringEntity se = new StringEntity(json, HTTP.UTF_8);
        se.setContentType(CONTENT_TYPE_TEXT_JSON);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
        httpPost.setEntity(se);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        return result;
    }
}
