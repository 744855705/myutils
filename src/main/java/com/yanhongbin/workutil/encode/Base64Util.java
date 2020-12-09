package com.yanhongbin.workutil.encode;

import java.nio.ByteBuffer;
import java.util.Base64;

/**
 * Created with IDEA
 * description : base64 工具类 基于 jdk1.8
 *
 * @author ：yanhongbin
 * @date : Created in 2020/8/24 5:38 下午
 */
public class Base64Util {

    public static final Base64.Decoder DECODER = Base64.getDecoder();
    public static final Base64.Encoder ENCODER = Base64.getEncoder();


    public static byte[] decode(String str) {
        return DECODER.decode(str);
    }

    public static byte[] decode(byte[] src) {
        return DECODER.decode(src);
    }

    public static ByteBuffer decode(ByteBuffer buffer) {
        return DECODER.decode(buffer);
    }

    public static String encodeToString(byte[] src) {
        return ENCODER.encodeToString(src);
    }

    public static byte[] encode(byte[] src) {
        return ENCODER.encode(src);
    }

    public static ByteBuffer encode(ByteBuffer buffer) {
        return ENCODER.encode(buffer);
    }

}
