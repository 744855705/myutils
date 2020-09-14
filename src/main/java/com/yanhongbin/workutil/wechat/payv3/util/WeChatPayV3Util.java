package com.yanhongbin.workutil.wechat.payv3.util;


import com.yanhongbin.workutil.encode.SecurityUtils;
import com.yanhongbin.workutil.http.HttpFluentUtil;
import com.yanhongbin.workutil.wechat.payv3.util.paramter.Amount;
import com.yanhongbin.workutil.wechat.payv3.util.paramter.Payer;
import com.yanhongbin.workutil.wechat.payv3.util.paramter.WeChatPayRequestParameter;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.math.BigDecimal;

/**
 * Created with IDEA
 * description : 微信支付 V3 工具类
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/1 11:23 上午
 */
public class WeChatPayV3Util {

    private static final String JS_API_PAY_URL = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";
    private static final String JS_API_PAY_ABS_URL = "/v3/pay/transactions/jsapi";


    /**
     * 构建 JS API 订单
     *
     * @param payRequestParameter 参数
     * @return String
     */
    public static String makeJsApiOrder(WeChatPayRequestParameter payRequestParameter, String serialNo, String privateKey) {
        try {
            Request post = HttpFluentUtil.createPost(JS_API_PAY_URL, payRequestParameter.toJSONObject());
            String nonceStr = SecurityUtils.md5Encode(payRequestParameter.getOutTradeNo());
            String token = WeChatPayV3SignUtil.getToken("POST", JS_API_PAY_ABS_URL, payRequestParameter.toJsonString(), payRequestParameter.getMchId(), nonceStr, serialNo, privateKey);

            post.addHeader("Authorization", token);
            post.addHeader("content-type", "application/json");
            Response execute = post.execute();

            HttpResponse httpResponse = execute.returnResponse();
            int statusCode = httpResponse.getStatusLine().getStatusCode();


            System.out.println(statusCode);
            return execute.returnContent().asString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构建 V3 支付基本必须参数
     *
     * @param appId       app id
     * @param mchId       商户号
     * @param description 商品描述
     * @param outTradeNo  商户订单号
     * @param notifyUrl   回调参数
     * @param total       支付金额
     * @return WeChatPayRequestParameter
     */
    public static WeChatPayRequestParameter makeBasePayParameter(String appId, String mchId, String description, String outTradeNo, String notifyUrl, BigDecimal total) {
        WeChatPayRequestParameter parameter = new WeChatPayRequestParameter();
        parameter.setAppId(appId);
        parameter.setMchId(mchId);
        parameter.setDescription(description);
        parameter.setOutTradeNo(outTradeNo);
        parameter.setNotifyUrl(notifyUrl);
        // 价格乘100取整，单位分
        parameter.setAmount(new Amount(total.multiply(BigDecimal.valueOf(100L)).intValue()));
        return parameter;
    }

    /**
     * 构建JS API 下单基本参数
     * {@link #makeBasePayParameter(String, String, String, String, String, BigDecimal)}
     *
     * @param openId 用户 openId
     * @return WeChatPayRequestParameter
     */
    public static WeChatPayRequestParameter makeJsApiBasePayParameter(String appId, String mchId, String description, String outTradeNo, String notifyUrl, BigDecimal total, String openId) {
        WeChatPayRequestParameter parameter = makeBasePayParameter(appId, mchId, description, outTradeNo, notifyUrl, total);
        parameter.setPayer(new Payer(openId));
        return parameter;
    }

}
