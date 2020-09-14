package com.yanhongbin.workutil.encode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IDEA
 * description : 加密工具类
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/28 11:59 上午
 */
public class SecurityUtils {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtils.class);


    public static String toHexString(byte[] md5Bytes) {
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 加密
     * @param inStr 字符串
     * @param encryptTypeName 加密方法名
     * @return 加密后的字符串
     */
    public static String encrypt(String inStr, String encryptTypeName) {
        return encrypt(inStr, SecurityType.getBySecurityTypeName(encryptTypeName));
    }

    public static String encrypt(String inStr, SecurityType securityType) {
        if (SecurityType.NONE.equals(securityType)) {
            return "";
        }
        log.info("使用加密方法：{}", securityType.getName());
        log.info("要加密的字符串：{}", inStr);
        MessageDigest instance = null;
        try {
            instance = MessageDigest.getInstance(securityType.getName());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        return toHexString(instance.digest(inStr.getBytes(StandardCharsets.UTF_8)));
    }

    public static String sha1Encode(String inStr) {
        return SecurityUtils.encrypt(inStr, SecurityType.SHA_1.getName());
    }

    public static String md5Encode(String inStr) {
        return SecurityUtils.encrypt(inStr, SecurityType.MD5.getName());
    }
}
