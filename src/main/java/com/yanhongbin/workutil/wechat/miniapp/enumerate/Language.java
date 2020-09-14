package com.yanhongbin.workutil.wechat.miniapp.enumerate;

import org.apache.commons.lang3.StringUtils;

/**
 * Created with IDEA
 * description: 语言类型枚举
 *
 * @author YanHongBin
 * @date Created in 2020/7/04 10:40
 */
public enum Language {


    /**
     * 简体中文
     */
    zh_CN("zh_CN"),

    /**
     * 英文
     */
    en_US("en_US"),

    /**
     * 繁体中文
     */
    zh_HK("zh_HK"),

    /**
     * 繁体中文
     */
    zh_TW("zh_TW"),
    ;
    private String language;

    Language(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return language;
    }

    public String getLanguage() {
        return language;
    }

    public static Language getLanguage(String language) {
        if (StringUtils.isBlank(language)|| StringUtils.isEmpty(language)) {
            return zh_CN;
        }
        try {
            return valueOf(language);
        } catch (Exception e) {
            return zh_CN;
        }
    }
}

