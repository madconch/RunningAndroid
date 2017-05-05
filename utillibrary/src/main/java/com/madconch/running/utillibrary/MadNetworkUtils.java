package com.madconch.running.utillibrary;

import android.content.Context;
import android.net.ConnectivityManager;

import com.madconch.running.utillibrary.config.MadUtilsConfig;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/4.
 * Email:496349136@qq.com
 */

public class MadNetworkUtils {
    /**
     * 当前是否有网络连接
     */
    public static boolean isConnected(Context context) {
        boolean flag;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        flag = manager != null && manager.getActiveNetworkInfo() != null;
        return flag;
    }

    public static boolean isConnected() {
        return isConnected(MadUtilsConfig.getContextProvider().provideContext());
    }
}
