package com.yanhongbin.workutil.wechat.payv3.result;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/1 2:07 下午
 */
public class JsApiOrderResult {

    private String prepayId;

    public JsApiOrderResult(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }
}
