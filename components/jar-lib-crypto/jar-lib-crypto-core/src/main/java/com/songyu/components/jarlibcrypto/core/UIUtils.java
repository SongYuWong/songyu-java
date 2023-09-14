package com.songyu.components.jarlibcrypto.core;

import com.songyu.commonutils.CommonDateUtils;
import com.songyu.commonutils.CommonRandomUtils;
import com.songyu.commonutils.CommonStringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

/**
 * <p>
 * 用户交互工具类
 * </p>
 *
 * @author songYu
 * @since 2023/9/8 17:53
 */
public class UIUtils {

    /**
     * 读取密码
     *
     * @return 读取到的密码
     */
    public static String readPassword() {
        Scanner scanner = new Scanner(System.in);
        String pass;
        // 使用密码进行后续操作
        do {
            System.out.println("请输入对应的加密密钥(不输入将自动生成随机密码，退出输入 0 ): ");
            pass = scanner.nextLine();
            if (Objects.equals(pass, "0")) {
                System.exit(0);
            } else if (CommonStringUtils.isBlank(pass)) {
                pass = CommonRandomUtils.randomString(12);
                System.err.println("随机密码：" + pass);
            }
        } while (!passValid(pass));
        return pass;
    }

    /**
     * 密码合规校验
     *
     * @param pass 密码
     * @return 是否合规
     */
    private static boolean passValid(String pass) {
        if (!CommonStringUtils.isBlank(pass)) {
            if (pass.length() >= 8) {
                return true;
            }
        }
        System.err.println("密码最少8位");
        return false;
    }

    public static Long readTimeEnd(Long begin) {
        Scanner scanner = new Scanner(System.in);
        String time;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        do {
            System.out.println("请输入授权结束时间（不输入回车表示开始时间一个小时后，退出输入 0）：");
            time = scanner.nextLine();
            if (Objects.equals(time, "0")) {
                System.exit(0);
            } else if (CommonStringUtils.isBlank(time)) {
                return CommonDateUtils.addHours(new Date(begin), 1).getTime();
            }
        } while (CommonDateUtils.checkDateFormat(time, dateFormat));
        try {
            return dateFormat.parse(time).getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long readTimeBegin() {
        Scanner scanner = new Scanner(System.in);
        String time;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        do {
            System.out.println("请输入授权起始时间（不输入回车表示立即开始授权，退出输入 0）：");
            time = scanner.nextLine();
            if (CommonStringUtils.isBlank(time)) {
                return System.currentTimeMillis();
            }
            if (Objects.equals(time, "0")) {
                System.exit(0);
            } else if (CommonStringUtils.isBlank(time)) {
                return System.currentTimeMillis();
            }
        } while (CommonDateUtils.checkDateFormat(time, dateFormat));
        try {
            return dateFormat.parse(time).getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
