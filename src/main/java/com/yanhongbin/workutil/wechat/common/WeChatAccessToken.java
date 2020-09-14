package com.yanhongbin.workutil.wechat.common;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IDEA
 * description:
 *
 * @author YanHongBin
 * @date Created in 2020/7/04 10:40
 */
public class WeChatAccessToken {

    private static final String ACCESS_TOKEN_KEY = "access_token";

    private static final String EXPIRES_IN_KEY = "expires_in";

    private final String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    private final Integer expiresIn;

    /**
     * access_token 和 expires_in 过期时间的封装
     *
     * @param jsonObject 调用接口的返回值
     */
    public WeChatAccessToken(JSONObject jsonObject) {
        this.accessToken = jsonObject.getString(ACCESS_TOKEN_KEY);
        this.expiresIn = jsonObject.getInteger(EXPIRES_IN_KEY);
    }
}
