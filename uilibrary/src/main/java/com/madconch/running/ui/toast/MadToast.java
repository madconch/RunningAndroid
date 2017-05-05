package com.madconch.running.ui.toast;

import android.support.annotation.StringRes;

import com.madconch.running.uiconfig.config.MadUIConfig;
import com.madconch.running.uiconfig.toast.IToastConfig;
import com.madconch.running.uiimpl.toast.ToastConfig;

/**
 * 功能描述:Toast工具
 * Created by LuoHaifeng on 2017/5/4.
 * Email:496349136@qq.com
 */

public class MadToast {
    private static IToastConfig config = new ToastConfig();

    public static void info(String message) {
        config.info(MadUIConfig.getContextProvider().provideContext(), message);
    }

    public static void info(@StringRes int message) {
        config.info(MadUIConfig.getContextProvider().provideContext(), MadUIConfig.getContextProvider().provideContext().getString(message));
    }

    public static void warn(String message) {
        config.warn(MadUIConfig.getContextProvider().provideContext(), message);
    }

    public static void warn(@StringRes int message) {
        config.warn(MadUIConfig.getContextProvider().provideContext(), MadUIConfig.getContextProvider().provideContext().getString(message));
    }

    public static void error(String message) {
        config.error(MadUIConfig.getContextProvider().provideContext(), message);
    }

    public static void error(@StringRes int message) {
        config.error(MadUIConfig.getContextProvider().provideContext(), MadUIConfig.getContextProvider().provideContext().getString(message));
    }
}
