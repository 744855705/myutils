package com.yanhongbin.workutil.wechat.payv3.util.paramter;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/1 12:05 下午
 */
public interface IJson {
    /**
     * 转化为json string
     * @return
     */
    String toJsonString();

    default JSONObject toJSONObject(){
        return JSONObject.parseObject(this.toJsonString());
    }
}
