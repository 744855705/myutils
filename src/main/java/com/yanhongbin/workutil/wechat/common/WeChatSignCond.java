package com.yanhongbin.workutil.wechat.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/29 4:19 下午
 */
public class WeChatSignCond {

    private String appId;

    private Long timeStamp;

    private String nonceStr;

    private String packageStr;

    private String signType;


    public Map<String, Object> toParamMap(){
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("appId", this.appId);
        map.put("timeStamp", this.timeStamp);
        map.put("nonceStr", this.nonceStr);
        map.put("package", this.packageStr);
        map.put("signType", this.signType);
        return map;
    }


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPackageStr() {
        return packageStr;
    }

    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

}
