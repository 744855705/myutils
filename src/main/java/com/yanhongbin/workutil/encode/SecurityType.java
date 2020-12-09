package com.yanhongbin.workutil.encode;


import org.apache.commons.lang3.StringUtils;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/29 4:52 下午
 */
public enum SecurityType {
    /**
     * 匹配失败
     */
    NONE(""),

    /**
     * MD5
     */
    MD5("MD5"),

    /**
     * SHA-1
     */
    SHA_1("SHA-1"),
    ;

    private final String name;

    SecurityType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SecurityType getBySecurityTypeName(String name) {
        if (StringUtils.isEmpty(name)) {
            return NONE;
        }
        for (SecurityType value : values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return NONE;
    }
}
