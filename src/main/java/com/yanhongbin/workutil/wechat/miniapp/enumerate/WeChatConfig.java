package com.yanhongbin.workutil.wechat.miniapp.enumerate;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/7/04 10:40
 */
public enum WeChatConfig {

    ;
    /**
     * app_id
     */
    private final String appId;

    /**
     * secret
     */
    private final String secret;


    WeChatConfig(String appId, String secret) {
        this.appId = appId;
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    public String getAppId() {
        return appId;
    }
}
