package com.madconch.running.utillibrary;

import java.util.Random;

/**
 * 功能描述:字符串工具
 * Created by LuoHaifeng on 2017/5/23.
 * Email:496349136@qq.com
 */

public class MadStringUtil {
    /**
     * 获取指定长度的随机字符串
     * @param length 指定长度
     * @return 随机字符串
     */
    public static String getRandomString(int length) {
        StringBuilder buffer = new StringBuilder("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i ++) {
            sb.append(buffer.charAt(random.nextInt(range)));
        }
        return sb.toString();
    }
}
