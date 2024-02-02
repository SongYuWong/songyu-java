package com.songyu.commonutils;

import java.util.Scanner;

/**
 * <p>
 * 通用控制台用户接口工具
 * </p>
 *
 * @author songYu
 * @since 2023/11/6 17:41
 */
public class CommonConsoleUIUtils {

    /**
     * 隐藏读取用户输入
     *
     * @param prompt 输入提示
     * @return 读取到的数据
     */
    public static String hiddenScanner(String prompt) {
        System.out.print(prompt);
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextLine();
        }
    }

}
