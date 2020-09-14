package com.yanhongbin.workutil.wechat.payv3.util.paramter;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/1 11:34 上午
 */
public class Payer implements IJson{
    private String openid;

    public Payer(String openid) {
        this.openid = openid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toJsonString() {
        JSONObject json = new JSONObject();
        json.put("openid", this.getOpenid());
        return json.toJSONString();
    }

}
