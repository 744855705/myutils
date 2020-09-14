package com.yanhongbin.workutil.wechat.payv3.util.paramter;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/1 11:34 上午
 */
public class Detail implements IJson{
    private String costPrice;
    private String invoiceId;
    private GoodsDetail goodsDetail;
    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public GoodsDetail getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(GoodsDetail goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    @Override
    public String toJsonString() {
        JSONObject json = new JSONObject();
        if (StringUtils.isNotEmpty(this.getCostPrice())) {
            json.put("cost_price", this.getCostPrice());
        }
        if (StringUtils.isNotEmpty(this.getInvoiceId())) {
            json.put("invoice_id", this.getInvoiceId());
        }
        if (this.getGoodsDetail() != null) {
            json.put("goods_detail", this.getGoodsDetail().toJSONObject());
        }
        return json.toJSONString();
    }
}
