package com.yanhongbin.workutil.encode.common;

/**
 * Created with IDEA
 * description :
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/24 5:47 下午
 */
public class RsaKeyPair {

    /**
     * 私钥
     */
    private final String privateKey;

    /**
     * 公钥
     */
    private final String publicKey;

    public RsaKeyPair(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

}
