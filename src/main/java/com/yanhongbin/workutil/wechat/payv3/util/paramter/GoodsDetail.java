package com.yanhongbin.workutil.wechat.payv3.util.paramter;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/1 12:05 下午
 */
public class GoodsDetail implements IJson{

    // merchant_goods_id
    private String merchantGoodsId;
    // wechatpay_goods_id
    private String weChatPayGoodsId;
    // 	goods_name
    private String goodsName;
    //	quantity
    private String quantity;
    //	unit_price int 分
    private int unitPrice;

    public String getMerchantGoodsId() {
        return merchantGoodsId;
    }

    public void setMerchantGoodsId(String merchantGoodsId) {
        this.merchantGoodsId = merchantGoodsId;
    }

    public String getWeChatPayGoodsId() {
        return weChatPayGoodsId;
    }

    public void setWeChatPayGoodsId(String weChatPayGoodsId) {
        this.weChatPayGoodsId = weChatPayGoodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toJsonString() {
        JSONObject json = new JSONObject();
        json.put("merchant_goods_id", this.getMerchantGoodsId());
        if (StringUtils.isNotEmpty(this.getWeChatPayGoodsId())) {
            json.put("wechatpay_goods_id", this.getWeChatPayGoodsId());
        }
        if (StringUtils.isNotEmpty(this.getGoodsName())) {
            json.put("goods_name", this.getGoodsName());
        }
        json.put("quantity", this.getQuantity());
        json.put("unit_price", this.getUnitPrice());
        return json.toJSONString();
    }
}
