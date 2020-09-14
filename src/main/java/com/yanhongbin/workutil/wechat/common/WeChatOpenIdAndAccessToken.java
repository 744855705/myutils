package com.yanhongbin.workutil.wechat.common;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/28 4:07 下午
 */
public class WeChatOpenIdAndAccessToken {

//    {
//  "access_token":"ACCESS_TOKEN",
//  "expires_in":7200,
//  "refresh_token":"REFRESH_TOKEN",
//  "openid":"OPENID",
//  "scope":"SCOPE"
//}

    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String EXPIRES_IN = "expires_in";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private static final String OPENID_KEY = "openid";
    private static final String SCOPE_KEY = "scope";

    private final String accessToken;
    private final Integer expiresIn;
    private final String refreshToken;
    private final String openid;
    private final String scope;

    public WeChatOpenIdAndAccessToken(JSONObject jsonObject) {
        this.accessToken = jsonObject.getString(ACCESS_TOKEN_KEY);
        this.expiresIn = jsonObject.getInteger(EXPIRES_IN);
        this.refreshToken = jsonObject.getString(REFRESH_TOKEN_KEY);
        this.openid = jsonObject.getString(OPENID_KEY);
        this.scope = jsonObject.getString(SCOPE_KEY);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getOpenid() {
        return openid;
    }

    public String getScope() {
        return scope;
    }
}
