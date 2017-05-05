package com.madconch.running.base.config;

import android.app.Application;
import android.content.Context;

import com.madconch.running.uiconfig.config.MadUIConfig;
import com.madconch.running.utillibrary.config.MadUtilsConfig;

/**
 * 功能描述:工具类初始化配置
 * Created by LuoHaifeng on 2017/5/4.
 * Email:496349136@qq.com
 */

public class BaseConfig {
    private static ContextProvider contextProvider;

    public static void init(final Application context) {
        MadUIConfig.init(context);
        MadUtilsConfig.init(context);
        contextProvider = new ContextProvider() {
            @Override
            public Context provideContext() {
                return context;
            }
        };
    }

    public static ContextProvider getContextProvider() {
        return contextProvider;
    }
}
