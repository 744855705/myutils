package com.yanhongbin.workutil.wechat.miniapp.enumerate;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/7/04 10:40
 */
public enum WeChatConfig {
    /**
     * 小程序配置
     */
    MINI_APP("wx4531d41e7518498c","1b639d49bad46c0909f3b85d63de1b64"),
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
