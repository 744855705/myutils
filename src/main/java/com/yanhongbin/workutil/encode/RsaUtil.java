package com.yanhongbin.workutil.encode;

import com.yanhongbin.workutil.encode.common.RsaKeyPair;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/24 5:57 下午
 */
public class RsaUtil {


    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 1170;

    /**
     * RSA最大解密密文大小
     */
	private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 加密
     * @param str 要加密的字符串
     * @param publicKey 公钥
     * @return 加密后的字符串
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64Util.decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        // 分段加密
        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        // 加密时超过117字节就报错。为此采用分段加密的办法来加密
        byte[] enBytes = null;
        for (int i = 0; i < data.length; i += MAX_ENCRYPT_BLOCK) {
            // 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + MAX_ENCRYPT_BLOCK));
            enBytes = ArrayUtils.addAll(enBytes, doFinal);
        }
        return Base64Util.encodeToString(enBytes);
    }

    /**
     * 解密
     * @param str 要解密的字符串
     * @param privateKey 私钥
     * @return 解密后的字符串
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //base64编码的私钥
        byte[] decoded = Base64Util.decode(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);

        // 分段解密
        // 64位解码加密后的字符串
        byte[] data = Base64Util.decode(str);
        // 解密时超过字节报错。为此采用分段解密的办法来解密
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i += MAX_DECRYPT_BLOCK) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + MAX_DECRYPT_BLOCK));
            sb.append(new String(doFinal));
        }

        return sb.toString();
    }

    public static RsaKeyPair genKeyPair(int keySize) throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器
        keyPairGen.initialize(keySize,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = new String(Base64Util.encode(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64Util.encode((privateKey.getEncoded())));
        return new RsaKeyPair(privateKeyString, publicKeyString);
    }

}
