package com.madconch.running.utillibrary;

import android.content.Context;

import com.madconch.running.utillibrary.config.MadUtilsConfig;

import java.io.File;

/**
 * 功能描述:缓存相关的工具
 * Created by LuoHaifeng on 2017/5/23.
 * Email:496349136@qq.com
 */

public class MadCacheUtil {
    /**
     * 获取缓存目录
     *
     * @return 缓存目录
     */
    public static File getCacheDir() {
        Context context = MadUtilsConfig.getContextProvider().provideContext();
        File file = context.getExternalCacheDir();
        if (file != null && file.exists()) {
            File cacheDir = new File(file, "cache");
            if (!cacheDir.exists()) {
                boolean flag = cacheDir.mkdirs();
                if (!flag) {
                    return file;
                }
            } else {
                return cacheDir;
            }
        }

        file = context.getCacheDir();
        if (file != null && file.exists()) {
            File cacheDir = new File(file, "cache");
            if (!cacheDir.exists()) {
                boolean flag = cacheDir.mkdirs();
                if (!flag) {
                    return file;
                }
            } else {
                return cacheDir;
            }
        }
        return file;
    }

    /***
     * 清除缓存目录
     */
    public static void clearCache() {
        File cacheDir = getCacheDir();
        if (cacheDir.exists() && cacheDir.isDirectory()) {
            File[] children = cacheDir.listFiles();
            if (children != null && children.length > 0) {
                for (File file : children) {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                }
            }
        }
    }
}
