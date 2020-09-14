package com.yanhongbin.workutil.wechat.payv3.util.paramter;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created with IDEA
 * description : 微信接口调用参数，大小写敏感，转JSON时参数名使用下划线
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/1 11:27 上午
 */
public class WeChatPayRequestParameter implements IJson {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
    private static final ZoneId zoneId = ZoneId.systemDefault();

    private String appId;

    private String mchId;

    private String description;

    private String outTradeNo;

    private Date timeExpire;

    private String attach;

    private String notifyUrl;

    private String goodsTag;

    private Amount amount;

    private Payer payer;

    private Detail detail;

    private SceneInfo sceneInfo;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Date getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(Date timeExpire) {
        this.timeExpire = timeExpire;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public SceneInfo getSceneInfo() {
        return sceneInfo;
    }

    public void setSceneInfo(SceneInfo sceneInfo) {
        this.sceneInfo = sceneInfo;
    }

    @Override
    public String toJsonString() {
        JSONObject json = new JSONObject();
        json.put("appid", this.getAppId());
        json.put("mchid", this.getMchId());
        json.put("description", this.getDescription());
        json.put("out_trade_no", this.getOutTradeNo());
        Date timeExpire = this.getTimeExpire();
        if (timeExpire != null) {
            json.put("time_expire", DATE_FORMATTER.format(timeExpire.toInstant().atZone(zoneId)));
        }
        if (StringUtils.isNotEmpty(this.getAttach())) {
            json.put("attach", this.getAttach());
        }
        json.put("notify_url", this.getNotifyUrl());
        if (StringUtils.isNotEmpty(this.getGoodsTag())) {
            json.put("goods_tag", this.getGoodsTag());
        }
        json.put("amount", this.getAmount().toJSONObject());
        if (this.getPayer() != null) {
            json.put("payer", this.getPayer().toJSONObject());
        }
        if (this.getDetail() != null) {
            json.put("detail", this.getDetail().toJSONObject());
        }
        if (this.getSceneInfo() != null) {
            json.put("scene_info", this.getSceneInfo().toJSONObject());
        }
        return json.toJSONString();
    }

}








