package com.songyu.commonutils;

import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.io.*;
import java.math.BigDecimal;
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
                oT = objectToByteArrayJsonS(j);
            } else {
                oT = objectToByteArrayJsonS(j.toString());
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

    @Data
    private static class Te {
        Object o;
    }

    /**
     * 对象转字节数组（Json 序列化）
     *
     * @param obj 对象
     * @return 字节数组
     */
    public static byte[] objectToByteArrayJsonS(Object obj) {
        Te t = new Te();
        t.setO(obj);
        return JSONUtil.toJsonStr(t).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 字节数组转对象（Json 序列化）
     *
     * @param bytes 字节数组
     * @param clazz 对象类
     * @param <T>   对象类型
     * @return 对象
     */
    public static <T> T byteArrayToObjectJsonS(byte[] bytes, Class<T> clazz) {
        if (clazz == Double.class) {
            return clazz.cast(((BigDecimal) JSONUtil.toBean(new String(bytes, StandardCharsets.UTF_8), Te.class).o).doubleValue());
        }
        if (clazz == Float.class) {
            return clazz.cast(((BigDecimal) JSONUtil.toBean(new String(bytes, StandardCharsets.UTF_8), Te.class).o).floatValue());
        }
        return clazz.cast(JSONUtil.toBean(new String(bytes, StandardCharsets.UTF_8), Te.class).o);
    }

    /**
     * 对象转字节数组（java 序列化）
     *
     * @param obj 对象
     * @return 字节数组
     */
    public static byte[] objectToByteArrayJavaS(Object obj) {
        try (ByteArrayOutputStream ob = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(ob)) {
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            ob.flush();
            return ob.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("对象序列化失败", e);
        }
    }

    /**
     * 字节数组转对象（java 序列化）
     *
     * @param bytes 字节数组
     * @param clazz 对象类
     * @param <T>   对象类型
     * @return 对象
     */
    public static <T> T byteArrayToObjectJavaS(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(in)) {
            return clazz.cast(objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("对象反序列化失败", e);
        }
    }

    /**
     * 字符串转字节数组
     *
     * @param str 字符串
     * @return 字节数组
     */
    public static byte[] stringToByteArray(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 字节数组转字符串
     *
     * @param bytes 字节数组
     * @return 字符串
     */
    public static String byteArrayToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
