package com.yanhongbin.workutil.wechat.miniapp;

import com.alibaba.fastjson.JSONObject;

import com.yanhongbin.workutil.http.HttpFluentUtil;
import com.yanhongbin.workutil.localcache.CacheUtil;
import com.yanhongbin.workutil.wechat.common.WeChatAccessToken;
import com.yanhongbin.workutil.wechat.miniapp.enumerate.Language;
import com.yanhongbin.workutil.wechat.miniapp.enumerate.MiniProgramState;
import com.yanhongbin.workutil.wechat.miniapp.enumerate.WeChatConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IDEA
 * description: 单例bean,微信小程序相关工具类
 *
 * @author YanHongBin
 * @date Created in 2020/7/04 10:40
 */
public class WeChatMiniAppUtil {

    private static final Logger log = LoggerFactory.getLogger(WeChatMiniAppUtil.class);

    /**
     * 微信小程序access_token 存储缓存中的key
     */
    private static final String WE_CHAT_MINI_APP_ACCESS_TOKEN_CACHE_KEY = "WE_CHAT_MINI_APP_ACCESS_TOKEN_CACHE_KEY";


    /**
     * 从微信获取 access_token
     *
     * @return WeChatMiniAppAccessToken
     */
    static private WeChatAccessToken getAccessToken(String appId, String secret) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
        String formatUrl = String.format(url, appId, secret);
        return new WeChatAccessToken(JSONObject.parseObject(HttpFluentUtil.doGet(formatUrl)));
    }

    /**
     * 缓存中获取access_token ,如果缓存中没有,从微信获取,并且刷新缓存
     *
     * @return access_token
     */
    static public String getAccessTokenInCache() {
        String accessTokenInCache = CacheUtil.get(WE_CHAT_MINI_APP_ACCESS_TOKEN_CACHE_KEY);
        if (StringUtils.isEmpty(accessTokenInCache)) {
            synchronized (WeChatMiniAppUtil.class) {
                // 二次校验,防止多个线程请求微信平添获取access_token
                String accessTokenInCacheSecond = CacheUtil.get(WE_CHAT_MINI_APP_ACCESS_TOKEN_CACHE_KEY);
                if (StringUtils.isEmpty(accessTokenInCacheSecond)) {
                    // 缓存中没有access_token,从微信平台重新获取
                    WeChatAccessToken accessToken = getAccessToken(WeChatConfig.MINI_APP.getAppId(), WeChatConfig.MINI_APP.getSecret());
                    // 放入缓存,过期时间设置为比微信少10秒,防止微信已过期但是缓存中未过期的情况
                    CacheUtil.put(WE_CHAT_MINI_APP_ACCESS_TOKEN_CACHE_KEY, accessToken.getAccessToken(), accessToken.getExpiresIn() - 10);
                    return accessToken.getAccessToken();
                }
                // 已经有其他的线程刷新了缓存,直接返回
                return accessTokenInCacheSecond;
            }
        }
        // 缓存中的accessToken没有过期,直接返回
        return accessTokenInCache;
    }


    /**
     * 推送小程序订阅消息
     *
     * @param accessToken 接口调用凭证 必要
     * @param openId      接收者（用户）的 openid 必要
     * @param templateId  所需下发的订阅模板id 必要
     * @param page        点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。 非必要
     * @param data        模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } } 必要
     * @param state       跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版 非必要,默认为正式版
     * @param language    zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN
     */
    static public void pushMessage(String accessToken, String openId, String templateId, String page, TemplateMessageData data, MiniProgramState state, Language language) {
        if (language == null) {
            // 不传默认简体中文
            language = Language.zh_CN;
        }
        if (state == null) {
            // 不传默认线上
            state = MiniProgramState.FORMAL;
        }
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken;
        JSONObject json = new JSONObject();
        json.put("touser", openId);
        json.put("template_id", templateId);
        if (page != null) {
            json.put("page", page);
        }
        if (data == null) {
            // 必传参数,没有内容则传空
            json.put("data", "");
        } else {
            json.put("data", data.getContent());
        }
        json.put("miniprogram_state", state.getState());
        json.put("lang", language.getLanguage());
        log.info("post body:{}", json);
        String post = HttpFluentUtil.post(url, json);
        log.info("微信推送接口返回值:{}", post);
    }

    /**
     * 不需要传入accessToken的推送方法
     *
     * @see WeChatMiniAppUtil#pushMessage(String, String, String, String, TemplateMessageData, MiniProgramState, Language)
     */
    static public void pushMessage(String openId, String templateId, String page, TemplateMessageData data, MiniProgramState state, Language language) {
        pushMessage(getAccessTokenInCache(), openId, templateId, page, data, state, language);
    }

    /**
     * 不需要传入accessToken,使用正式环境,默认语言的推送方法
     *
     * @see WeChatMiniAppUtil#pushMessage(String, String, String, TemplateMessageData, MiniProgramState, Language)
     */
    static public void pushMessage(String openId, String templateId, String page, TemplateMessageData data) {
        pushMessage(openId, templateId, page, data, null, null);
    }

    /**
     * 不需要传入accessToken,使用正式环境,默认语言,无页面跳转的推送方法
     *
     * @see WeChatMiniAppUtil#pushMessage(String, String, String, TemplateMessageData)
     */
    static public void pushMessage(String openId, String templateId, TemplateMessageData data) {
        pushMessage(openId, templateId, null, data);
    }

}
