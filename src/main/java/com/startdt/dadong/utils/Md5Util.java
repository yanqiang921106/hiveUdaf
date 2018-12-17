package com.startdt.dadong.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * @ClassName Md5Util
 * @Author xucanming
 * @Date 2018/11/30 11:06
 * @Version 1.0
 **/


public class Md5Util {


    private static MessageDigest md5;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算字符串的Md5值
     * @param string
     * @return
     */
    public static String getMd5(String string) {
        try {
            byte[] bs = md5.digest(string.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(40);
            for (byte x : bs) {
                if ((x & 0xff) >> 4 == 0) {
                    sb.append("0").append(Integer.toHexString(x & 0xff));
                } else {
                    sb.append(Integer.toHexString(x & 0xff));
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) throws Exception {
//        System.out.println(getMd5("HelloWorld"));
//    }
}
