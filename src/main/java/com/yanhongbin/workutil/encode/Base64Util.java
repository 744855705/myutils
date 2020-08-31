package com.yanhongbin.workutil.encode;

import java.nio.ByteBuffer;
import java.util.Base64;

/**
 * Created with IDEA
 * description : base64 工具类 基于 jdk1.8
 * 由于 JDK 1.8 提供的{@link Base64.Decoder} 和 {@link Base64.Encoder} 存在并发问题，故不能使用单例，每次编码都使用新的对象
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/24 5:38 下午
 */
public class Base64Util {


    public static byte[] decode(String str){
        return Base64.getDecoder().decode(str);
    }

    public static byte[] decode(byte[] src){
        return Base64.getDecoder().decode(src);
    }

    public static ByteBuffer decode(ByteBuffer buffer){
        return Base64.getDecoder().decode(buffer);
    }

    public static String encodeToString(byte[] src) {
        return Base64.getEncoder().encodeToString(src);
    }

    public static byte[] encode(byte[] src){
        return Base64.getEncoder().encode(src);
    }

    public static ByteBuffer encode(ByteBuffer buffer) {
        return Base64.getEncoder().encode(buffer);
    }

}
