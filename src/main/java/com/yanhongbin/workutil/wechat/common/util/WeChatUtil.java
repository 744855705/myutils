package com.yanhongbin.workutil.wechat.common.util;

import com.alibaba.fastjson.JSONObject;

import com.yanhongbin.workutil.encode.SecurityUtils;
import com.yanhongbin.workutil.http.HttpFluentUtil;
import com.yanhongbin.workutil.localcache.CacheKeyUtil;
import com.yanhongbin.workutil.localcache.CacheUtil;
import com.yanhongbin.workutil.localcache.IRefresh;
import com.yanhongbin.workutil.wechat.common.WeChatAccessToken;
import com.yanhongbin.workutil.wechat.common.WeChatJsApiTicket;
import com.yanhongbin.workutil.wechat.common.WeChatOpenIdAndAccessToken;
import com.yanhongbin.workutil.wechat.common.WeChatUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/28 11:27 上午
 */
public class WeChatUtil {

    private static final Logger log = LoggerFactory.getLogger(WeChatUtil.class);

    private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static final String GET_JS_API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
    private static final String JS_SDK_SIGNATURE_TEMPLATE = "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s";

    private static final String GET_ACCESS_TOKEN_OPEN_ID_BY_CODE_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    private static final String REFRESH_ACCESS_TOKEN_OPEN_ID_BY_CODE_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
    private static final String GET_WE_CHAT_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";


    /**
     * 从微信获取 access_token
     *
     * @return WeChatMiniAppAccessToken
     */
    static private WeChatAccessToken getAccessToken(String appId, String secret) {
        String formatUrl = String.format(GET_ACCESS_TOKEN_URL, appId, secret);
        return new WeChatAccessToken(JSONObject.parseObject(HttpFluentUtil.doGet(formatUrl)));
    }



    /**
     * 缓存中获取access_token ,如果缓存中没有,从微信获取,并且刷新缓存
     *
     * @return access_token
     */
    static public String getAccessTokenInCache(String appId, String secret) {
        return CacheUtil.getAndRefresh(CacheKeyUtil.makeWeChatAccessTokenCacheKey(appId, secret), new IRefresh<String>() {
            private final WeChatAccessToken accessToken =  WeChatUtil.getAccessToken(appId, secret);
            @Override
            public String getContent() {
                return accessToken.getAccessToken();
            }
            @Override
            public int getExpire() {
                return accessToken.getExpiresIn() - 10;
            }
        });
    }

    /**
     * 从微信获取 js api ticket
     * @param accessToken accessToken
     * @return WeChatJsApiTicket
     */
    static private WeChatJsApiTicket getJsApiTicket(String accessToken) {
        String formatUrl = String.format(GET_JS_API_TICKET_URL, accessToken);
        return new WeChatJsApiTicket(JSONObject.parseObject(HttpFluentUtil.doGet(formatUrl)));
    }

    /**
     * 从缓存获取 js api ticket
     * @param accessToken accessToken
     * @return String ticket
     */
    static public String getJsApiTicketInCache(String accessToken) {
        return CacheUtil.getAndRefresh(CacheKeyUtil.makeWeChatJsApiTicketCacheKey(accessToken), new IRefresh<String>() {
            private final WeChatJsApiTicket jsApiTicket = WeChatUtil.getJsApiTicket(accessToken);
            @Override
            public String getContent() {
                return jsApiTicket.getTicket();
            }
            @Override
            public int getExpire() {
                return jsApiTicket.getExpiresIn() - 10;
            }
        });
    }

    /**
     * js api signature 生成
     * @param jsApiTicket  jsApiTicket
     * @param nonceStr 随机字符串
     * @param timestamp 时间戳
     * @param url 当前页面地址
     * @return signature
     */
    static public String makeSignature(String jsApiTicket, String nonceStr, String timestamp, String url) {
        String format = String.format(JS_SDK_SIGNATURE_TEMPLATE, jsApiTicket, nonceStr, timestamp, url);
        try {
            return SecurityUtils.sha1Encode(format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static public WeChatOpenIdAndAccessToken getAccessTokenOpenIdByCode(String appId, String secret, String code) {
        String formatUrl = String.format(GET_ACCESS_TOKEN_OPEN_ID_BY_CODE_URL, appId, secret, code);
        String get = HttpFluentUtil.doGet(formatUrl);
        log.info("getAccessTokenOpenIdByCode.result:{}", get);
        return new WeChatOpenIdAndAccessToken(JSONObject.parseObject(get));
    }

    static public WeChatOpenIdAndAccessToken refreshAccessTokenOpenIdByCode(String appId, String refreshToken) {
        String formatUrl = String.format(REFRESH_ACCESS_TOKEN_OPEN_ID_BY_CODE_URL, appId, refreshToken);
        String get = HttpFluentUtil.doGet(formatUrl);
        log.info("getAccessTokenOpenIdByCode.result:{}", get);
        return new WeChatOpenIdAndAccessToken(JSONObject.parseObject(get));
    }

    static public WeChatOpenIdAndAccessToken refreshAccessTokenOpenIdCache(String appId, String refreshToken) {
        return CacheUtil.getAndRefresh(CacheKeyUtil.makeWeChatClientAccessTokenCacheKey(appId, refreshToken), new IRefresh<WeChatOpenIdAndAccessToken>() {
            private final WeChatOpenIdAndAccessToken weChatOpenIdAndAccessToken = WeChatUtil.refreshAccessTokenOpenIdByCode(appId, refreshToken);
            @Override
            public WeChatOpenIdAndAccessToken getContent() {
                return weChatOpenIdAndAccessToken;
            }
            @Override
            public int getExpire() {
                return weChatOpenIdAndAccessToken.getExpiresIn() - 10;
            }
        });
    }

    static public WeChatUserInfo getWeChatUserInfo(String appId, String secret, String openId) {
        String formatUrl = String.format(GET_WE_CHAT_USER_INFO_URL, getAccessTokenInCache(appId, secret), openId);
        return new WeChatUserInfo(JSONObject.parseObject(HttpFluentUtil.doGet(formatUrl)));
    }

    /**
     * 生成MD5微信签名
     * @param paramMap 参数Map
     * @param encodeKey 签名KEY
     * @return String
     */
    static public String makeMd5Sign(Map<String, Object> paramMap, String encodeKey) {
        Set<String> strings = paramMap.keySet();
        LinkedList<String> keys = new LinkedList<>(strings);
        Collections.sort(keys);
        log.info("keys排序后:{}", keys);
        StringBuilder originSignStr = new StringBuilder();
        for (String key : keys) {
            Object value = paramMap.get(key);
            if (value == null) {
                break;
            } else if (value instanceof String && StringUtils.isEmpty((CharSequence) value)) {
                break;
            }
            originSignStr.append(key).append('=').append(value).append('&');
        }
        originSignStr.append("key").append('=').append(encodeKey);
        return SecurityUtils.md5Encode(originSignStr.toString()).toUpperCase();
    }


}
