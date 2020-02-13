package cn.edu.buaa.onlinejudge.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MD5Util {
    /**
     * 默认密钥
     */
    private static String KEY = "BUAAOJ@accounts#";

    /**
     * 使用默认的密钥(KEY)生成的密文
     * @param text - 明文
     * @return 密文
     */
    public static String encryptedByMD5(String text) {
        return encryptedByMD5(text, KEY);
    }

    /**
     * 使用指定的密钥生成密文
     * @param text - 明文
     * @param key - 密钥
     * @return - 密文
     */
    public static String encryptedByMD5(String text, String key) {
        try {
            text += key;
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            Base64.Encoder base64en = Base64.getEncoder();
            String encryptedStr = base64en.encodeToString(md5.digest(text.getBytes("utf-8")));
            return encryptedStr;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证MD5加密算法，使用默认密钥
     * @param text - 明文
     * @param md5Str - 已加密的密文
     * @return 是否通过验证
     */
    public static boolean verifyMD5(String text, String md5Str) {
        return verifyMD5(text, KEY, md5Str);
    }
    /**
     * 验证MD5加密算法，使用指定密钥
     * @param text - 明文
     * @param key - 密钥
     * @param md5Str - 已加密的密文
     * @return 是否通过验证
     */
    public static boolean verifyMD5(String text, String key, String md5Str) {
        String encryptedStr = encryptedByMD5(text, key);
        if( encryptedStr.equals(md5Str) )
            return true;
        else
            return false;
    }
}