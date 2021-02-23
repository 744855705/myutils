package com.yanhongbin.workutil.wechat.miniapp;

import com.alibaba.fastjson.JSONObject;

import com.yanhongbin.workutil.http.HttpFluentUtil;
import com.yanhongbin.workutil.log.LogUtil;
import com.yanhongbin.workutil.wechat.common.util.WeChatUtil;
import com.yanhongbin.workutil.wechat.miniapp.enumerate.Language;
import com.yanhongbin.workutil.wechat.miniapp.enumerate.MiniProgramState;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IDEA
 * description: 单例bean,微信小程序相关工具类
 *
 * @author YanHongBin
 * @date Created in 2020/7/04 10:40
 */
@Slf4j
public class WeChatMiniAppUtil {

    /**
     * 从微信获取 access_token
     *
     * @return WeChatMiniAppAccessToken
     */
    static private String getAccessToken(String appId, String secret) {
        return WeChatUtil.getAccessTokenInCache(appId, secret);
    }
    static private String getAccessToken(MiniAppConfig miniAppConfig) {
        return WeChatUtil.getAccessTokenInCache(miniAppConfig.getAppId(), miniAppConfig.getSecret());
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
        LogUtil.info(log, "post body:{}", json);
        String post = HttpFluentUtil.post(url, json);
        LogUtil.info(log, "微信推送接口返回值:{}", post);
    }

    /**
     * 不需要传入accessToken的推送方法
     *
     * @see WeChatMiniAppUtil#pushMessage(String, String, String, String, TemplateMessageData, MiniProgramState, Language)
     */
    static public void pushMessage(MiniAppConfig miniAppConfig, String openId, String templateId, String page, TemplateMessageData data, MiniProgramState state, Language language) {
        pushMessage(getAccessToken(miniAppConfig), openId, templateId, page, data, state, language);
    }

    /**
     * 不需要传入accessToken,使用正式环境,默认语言的推送方法
     *
     * @see WeChatMiniAppUtil#pushMessage(MiniAppConfig, String, String, String, TemplateMessageData, MiniProgramState, Language)
     */
    static public void pushMessage(MiniAppConfig miniAppConfig, String openId, String templateId, String page, TemplateMessageData data) {
        pushMessage(miniAppConfig, openId, templateId, page, data, null, null);
    }

    /**
     * 不需要传入accessToken,使用正式环境,默认语言,无页面跳转的推送方法
     *
     * @see WeChatMiniAppUtil#pushMessage(MiniAppConfig, String, String, String, TemplateMessageData)
     */
    static public void pushMessage(MiniAppConfig miniAppConfig, String openId, String templateId, TemplateMessageData data) {
        pushMessage(miniAppConfig, openId, templateId, null, data);
    }

}
