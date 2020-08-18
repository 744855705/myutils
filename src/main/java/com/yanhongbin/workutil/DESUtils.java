package com.yanhongbin.workutil;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

@SuppressWarnings("restriction")
public class DESUtils {
	private static final Key KEY;
	private static final String KEY_STR = "Zuibon888888";

	static {
		try {
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(KEY_STR.getBytes());
			generator.init(secureRandom);
			KEY = generator.generateKey();
			generator = null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 对字符串进行加密，返回BASE64的加密字符串 <功能详细描述>
	 *
	 * @param str
	 * @return
	 */
	public static String getEncryptString(String str) {
		try {
			byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, KEY);
			byte[] encryptStrBytes = cipher.doFinal(strBytes);
			return new String(Base64.getEncoder().encode(encryptStrBytes));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 对BASE64加密字符串进行解密
	 *
	 * @param str
	 * @return
	 */
	public static String getDecryptString(String str) {
		try {
            byte[] strBytes = Base64.getDecoder().decode(str);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, KEY);
			byte[] encryptStrBytes = cipher.doFinal(strBytes);
			return new String(encryptStrBytes, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public static void main(String[] args) {
	}
}
