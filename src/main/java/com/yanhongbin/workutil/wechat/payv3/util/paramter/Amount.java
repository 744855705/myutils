package com.yanhongbin.workutil.wechat.payv3.util.paramter;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/1 11:34 上午
 */
public class Amount implements IJson{
    private int total;
    private String currency;

    public Amount(int total, String currency) {
        this.total = total;
        this.currency = currency;
    }

    public Amount(int total) {
        this.total = total;
        this.currency = "CNY";
    }
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }

}
