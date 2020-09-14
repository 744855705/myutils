package com.yanhongbin.workutil.wechat.miniapp;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IDEA
 * description: 微信小程序模板消息封装,以拼接特殊的json串
 *
 * @author YanHongBin
 * @date Created in 2020/7/04 10:40
 */
public class TemplateMessageData {

    private JSONObject json;

    public TemplateMessageData(){
        json = new JSONObject();
    }

    /**
     * 放入小程序模板参数 示例 {{amount9.DATA}}
     * @param key key 示例 amount9
     * @param value 实际放入的值
     */
    public void put(String key, Object value){
        JSONObject temp = new JSONObject();
        temp.put("value", String.valueOf(value));
        json.put(key, temp);
    }

    @Override
    public String toString() {
        return json.toJSONString();
    }

    public JSONObject getContent(){
        return json;
    }
}
