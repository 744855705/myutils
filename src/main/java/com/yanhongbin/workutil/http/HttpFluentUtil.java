package com.yanhongbin.workutil.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/6/5 6:19 下午
 */
public class HttpFluentUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpFluentUtil.class);

    public static String doGet(String url) {
        try {
            return Request.Get(url).execute().returnContent().asString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doGet(String url, Map<String, String> paramMap) {
        return doGet(url, null, null, null, paramMap);
    }

    public static String doGet(String url, String hostName, Integer port, String schemeName, Map<String, String> paramMap) {
        return executeGet(url, hostName, port, schemeName, paramMap);
    }

    private static String executeGet(String url, String hostName, Integer port, String schemeName, Map<String, String> paramMap) {
        Args.notNull(url, "url");

        url = buildGetParam(url, paramMap);
        Request request = Request.Get(url);
        request = buildProxy(request, hostName, port, schemeName);
        try {
            Response execute = request.execute();
            return execute.returnContent().asString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String buildGetParam(String url, Map<String, String> paramMap) {
        Args.notNull(url, "url");
        if (!paramMap.isEmpty()) {
            List<NameValuePair> paramList = new ArrayList<>(paramMap.size());
            for (String key : paramMap.keySet()) {
                paramList.add(new BasicNameValuePair(key, paramMap.get(key)));
            }
            //拼接参数
            url += "?" + URLEncodedUtils.format(paramList, Consts.UTF_8);
        }
        return url;
    }

    /**
     * 设置代理
     *
     * @param request
     * @param hostName
     * @param port
     * @param schemeName
     * @return
     */
    private static Request buildProxy(Request request, String hostName, Integer port, String schemeName) {
        if (StringUtils.isNotEmpty(hostName) && port != null) {
            //设置代理
            if (StringUtils.isEmpty(schemeName)) {
                schemeName = HttpHost.DEFAULT_SCHEME_NAME;
            }
            request.viaProxy(new HttpHost(hostName, port, schemeName));
        }
        return request;
    }


    public static Request createPost(String url, JSONObject param) {
        String string = param.toJSONString();
        System.out.println(string);
        return Request.Post(url).bodyString(string, ContentType.APPLICATION_JSON);
    }

    public static Request createPost(String url, Map<String, Object> param) {
        return createPost(url, new JSONObject(param));
    }

    public static String post(String url, JSONObject param){
        try {
            return createPost(url, param).execute().returnContent().asString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String post(String url, Map<String, Object> param) {
        return post(url, new JSONObject(param));
    }


    public static String postParam(String url, Map<String, String> param) {
        try {
            return Request.Post(buildGetParam(url, param)).execute().returnContent().asString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
