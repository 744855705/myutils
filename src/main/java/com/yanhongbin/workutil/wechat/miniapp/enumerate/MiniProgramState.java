package com.yanhongbin.workutil.wechat.miniapp.enumerate;

import org.apache.commons.lang3.StringUtils;

/**
 * Created with IDEA
 * description: 小程序版本枚举
 *
 * @author YanHongBin
 * @date Created in 2020/7/04 10:40
 */
public enum MiniProgramState {

    /**
     * 开发板
     */
    DEVELOPER("developer"),

    /**
     * 体验版
     */
    TRIAL("trial"),

    /**
     * 正式版
     */
    FORMAL("formal"),
    ;
    private String state;

    MiniProgramState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state;
    }

    public String getState() {
        return state;
    }

    public static MiniProgramState getState(String miniProgramState) {
        if (StringUtils.isBlank(miniProgramState)|| StringUtils.isEmpty(miniProgramState)) {
            return DEVELOPER;
        }
        try {
            return valueOf(miniProgramState);
        } catch (Exception e) {
            return DEVELOPER;
        }

    }
}
