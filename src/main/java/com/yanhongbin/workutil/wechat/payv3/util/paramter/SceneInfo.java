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
public class SceneInfo implements IJson {
    private String payerClientIp;

    private String deviceId;

    private StoreInfo storeInfo;

    public String getPayerClientIp() {
        return payerClientIp;
    }

    public void setPayerClientIp(String payerClientIp) {
        this.payerClientIp = payerClientIp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public StoreInfo getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(StoreInfo storeInfo) {
        this.storeInfo = storeInfo;
    }


    @Override
    public String toJsonString() {
        JSONObject json = new JSONObject();
        json.put("payer_client_ip", this.getPayerClientIp());
        if (StringUtils.isNotEmpty(this.getDeviceId())) {
            json.put("device_id", this.getDeviceId());
        }
        if (this.getStoreInfo() != null) {
            json.put("store_info", this.getStoreInfo().toJSONObject());
        }
        return json.toJSONString();
    }
}
