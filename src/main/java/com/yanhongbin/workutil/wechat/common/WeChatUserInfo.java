package com.yanhongbin.workutil.wechat.common;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/28 4:40 下午
 */
public class WeChatUserInfo {

    private String openid;
    private String nickname;
    private int sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private String privilege;
    private String unionid;

    public WeChatUserInfo(JSONObject jsonObject) {
        this.openid = jsonObject.getString("openid");
        this.nickname = jsonObject.getString("nickname");
        this.sex = jsonObject.getInteger("sex");
        this.province = jsonObject.getString("province");
        this.city = jsonObject.getString("city");
        this.country = jsonObject.getString("country");
        this.headimgurl = jsonObject.getString("headimgurl");
        this.privilege = jsonObject.getString("privilege");
        this.unionid = jsonObject.getString("unionid");
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
