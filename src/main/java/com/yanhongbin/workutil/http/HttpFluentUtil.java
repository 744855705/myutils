package com.yanhongbin.workutil.http;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
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
import java.nio.charset.Charset;
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
@Slf4j
public class HttpFluentUtil {


    /**
     * 默认连接超时时间
     */
    private static final int DEFAULT_CONNECT_TIMEOUT = 1000;

    /**
     * 默认socket超时时间
     */
    private static final int DEFAULT_SOCKET_TIMEOUT = 1000;

    /**
     * ThreadLocal变量 Charset
     */
    private static final ThreadLocal<Charset> CHARSET_LOCAL = new ThreadLocal<>();

    /**
     * ThreadLocal变量 connectTimeout
     */
    private static final ThreadLocal<Integer> CONNECT_TIMEOUT_LOCAL = new ThreadLocal<>();

    /**
     * ThreadLocal变量 socketTimeout
     */
    private static final ThreadLocal<Integer> SOCKET_TIMEOUT_LOCAL = new ThreadLocal<>();



    public static String doGet(String url) {
        return execute(Request.Get(url));
    }

    public static String doGet(String url, Map<String, String> paramMap) {
        return doGet(url, null, null, null, paramMap);
    }

    public static String doGet(String url, String hostName, Integer port, String schemeName, Map<String, String> paramMap) {
        return executeGet(url, hostName, port, schemeName, paramMap);
    }

    private static String executeGet(String url, String hostName, Integer port, String schemeName, Map<String, String> paramMap) {
        Args.notNull(url, "url");
        return execute(Request.Get(buildGetParam(url, paramMap)));
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

    public static Request createPost(String url, JSONObject param) {
        String string = param.toJSONString();
        Request post = Request.Post(url);
        return post.bodyString(string, ContentType.APPLICATION_JSON);
    }

    public static Request createPost(String url, Map<String, Object> param) {
        return createPost(url, new JSONObject(param));
    }

    public static String post(String url, JSONObject param){
        return execute(createPost(url, param));
    }

    @SuppressWarnings("unchecked")
    public static <T> String post(String url, Map<String, T> param) {
        return post(url, new JSONObject((Map<String, Object>) param));
    }


    public static String postParam(String url, Map<String, String> param) {
        return execute(Request.Post(buildGetParam(url, param)));
    }

    public static String put(String url) {
        return execute(Request.Put(url));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Object> String put(String url, Map<String, T> param) {
        Request put = createPut(url, new JSONObject((Map<String, Object>) param));
        return execute(put);
    }

    public static String put(String url, JSONObject param) {
        Request put = createPut(url, param);
        return execute(put);
    }

    private static Request createPut(String url, JSONObject jsonObject) {
        return Request.Put(url).bodyString(jsonObject.toJSONString(), ContentType.APPLICATION_JSON);
    }

    /**
     * 处理过期时间,提交request并返回字符串,处理ThreadLocal变量
     * @param request request
     * @return returnContent
     */
    public static String execute(Request request) {
        try {
            processTimeOut(request);
            return request.execute().returnContent().asString(getCharsetOrDefault());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            clearThreadLocal();
        }
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

    /**
     * 设置默认的请求超时时间
     * @param request r
     */
    private static void processTimeOut(Request request) {
        request.connectTimeout(getConnectTimeoutOrDefault()).socketTimeout(getSocketTimeoutOrDefault());
    }

    /**
     * 如果要指定当前请求返回值编码,调用该方法,不调用默认使用 UTF-8
     * @param charset 编码类型
     */
    public static void setCharSet(Charset charset) {
        CHARSET_LOCAL.remove();
        CHARSET_LOCAL.set(charset);
    }

    /**
     * 获取本次调用返回值编码 未设置过默认使用 UTF-8
     * @return Charset
     */
    public static Charset getCharsetOrDefault() {
        Charset charset = CHARSET_LOCAL.get();
        return charset == null ? Consts.UTF_8 : charset;
    }

    /**
     * 设置 connectTimeout
     * @param connectTimeout 连接过期时间
     */
    public static void setConnectTimeout(int connectTimeout) {
        CONNECT_TIMEOUT_LOCAL.remove();
        CONNECT_TIMEOUT_LOCAL.set(connectTimeout);
    }

    /**
     * 获取connectTimeout,未设置过使用默认
     * @return connectTimeout
     */
    private static Integer getConnectTimeoutOrDefault(){
        Integer connectTimeout = CONNECT_TIMEOUT_LOCAL.get();
        return connectTimeout == null ? DEFAULT_CONNECT_TIMEOUT : connectTimeout;
    }

    /**
     * 设置 socketTimeout
     * @param socketTimeout socketTimeout
     */
    public static void setSocketTimeout(int socketTimeout) {
        SOCKET_TIMEOUT_LOCAL.remove();
        SOCKET_TIMEOUT_LOCAL.set(socketTimeout);
    }

    /**
     * 获取socketTimeout,未设置过使用默认
     * @return socketTimeout
     */
    private static Integer getSocketTimeoutOrDefault(){
        Integer socketTimeout = SOCKET_TIMEOUT_LOCAL.get();
        return socketTimeout == null ? DEFAULT_SOCKET_TIMEOUT : socketTimeout;
    }

    /**
     * 调用后清除ThreadLocal内变量
     */
    private static void clearThreadLocal() {
        CHARSET_LOCAL.remove();
        CONNECT_TIMEOUT_LOCAL.remove();
        SOCKET_TIMEOUT_LOCAL.remove();
    }

}
