package com.songyu.commonutils;

import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 通用字节数组工具类
 * </p>
 *
 * @author songYu
 * @since 2023/9/13 10:46
 */
public class CommonByteArrayUtils {

    /**
     * 对象数组转固定长度的字节数组
     *
     * @param size    字节数组长度
     * @param objects 对象数组
     * @return 固定长度的字节数组
     */
    public static byte[] parseBytes(int size, Object[] objects) {
        byte[] b = new byte[size];
        int f = 0;
        for (Object j : objects) {
            if (j == null) {
                j = 2317775974L;
            }
            byte[] oT;
            if (f++ % 2 == 0) {
                oT = objectToByteArray(j);
            } else {
                oT = objectToByteArray(j.toString());
            }
            if (oT.length == b.length) {
                int i = 0;
                int q = b.length - 1;
                while (i < b.length) {
                    b[i] = (byte) (oT[i] ^ b[q]);
                    i++;
                    q--;
                }
            } else {
                if (oT.length > b.length) {
                    for (int i = 0; i < b.length; i++) {
                        b[i] = (byte) (oT[i] ^ b[i]);
                    }
                    int i = 0;
                    for (int k = 0; k < oT.length - b.length; k++) {
                        b[i] = (byte) (oT[b.length + k] ^ b[i]);
                        if (++i >= b.length) {
                            i = 0;
                        }
                    }
                } else {
                    int k = 0;
                    for (int i = 0; i < b.length; i++) {
                        b[i] = (byte) (b[i] ^ oT[k]);
                        if (++k >= oT.length) {
                            k = 0;
                        }
                    }
                }
            }
        }
        return b;
    }

    /**
     * 对象转字节数组
     *
     * @param obj 对象
     * @return 字节数组
     */
    public static byte[] objectToByteArray(Object obj) {
        return JSONUtil.toJsonStr(obj).getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] stringToByteArray(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    public static String byteArrayToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
