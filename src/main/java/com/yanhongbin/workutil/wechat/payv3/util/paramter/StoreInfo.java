package com.yanhongbin.workutil.wechat.payv3.util.paramter;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/1 1:31 下午
 */
public class StoreInfo implements IJson {

    private String id;

    private String name;

    private String areaCode;

    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toJsonString() {
        JSONObject json = new JSONObject();
        json.put("name", this.getName());
        json.put("area_code", this.getAreaCode());
        json.put("address", this.getAddress());
        if (StringUtils.isNotEmpty(this.getId())) {
            json.put("id", this.getId());
        }
        return json.toJSONString();
    }
}
