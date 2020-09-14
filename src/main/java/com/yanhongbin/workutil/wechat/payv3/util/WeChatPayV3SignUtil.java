package com.yanhongbin.workutil.wechat.payv3.util;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/9/1 3:21 下午
 */
public class WeChatPayV3SignUtil {

    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final String AUTH_TYPE = "WECHATPAY2-SHA256-RSA2048";

    public static String getToken(String method, String canonicalUrl, String body, String mchId, String nonceStr, String serialNo, String privateKey) {
        long timestamp = System.currentTimeMillis() / 1000;
        String message = buildMessage(method, canonicalUrl, timestamp, nonceStr, body);
        String signature = sign(message.getBytes(StandardCharsets.UTF_8), getPrivateKey(privateKey));
        return AUTH_TYPE + " mchid=\"" + mchId + "\","
            + "nonce_str=\"" + nonceStr + "\","
            + "timestamp=\"" + timestamp + "\","
            + "serial_no=\"" + serialNo + "\","
            + "signature=\"" + signature + "\"";
    }

    public static String buildMessage(String method, String canonicalUrl, long timestamp, String nonceStr, String body) {
        return method + "\n"
            + canonicalUrl + "\n"
            + timestamp + "\n"
            + nonceStr + "\n"
            + body + "\n";
    }

    public static String sign(byte[] message, PrivateKey yourPrivateKey) {
        try {
            Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            sign.initSign(yourPrivateKey);
            sign.update(message);
            return Base64.getEncoder().encodeToString(sign.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrivateKey getPrivateKey(String key) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
