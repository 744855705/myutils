package com.yanhongbin.workutil.wechat.common;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/28 4:17 下午
 */
public class WeChatOpenIdAndAccessTokenVO {

    private String accessToken;
    private Integer expiresIn;
    private String refreshToken;
    private String openid;
    private String scope;

    public WeChatOpenIdAndAccessTokenVO(WeChatOpenIdAndAccessToken token) {
        this.accessToken = token.getAccessToken();
        this.expiresIn = token.getExpiresIn();
        this.refreshToken = token.getRefreshToken();
        this.openid = token.getOpenid();
        this.scope = token.getScope();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
