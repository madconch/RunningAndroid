package com.madconch.running.uiconfig.config;

import android.app.Application;
import android.content.Context;

/**
 * 功能描述:UI类初始化配置
 * Created by LuoHaifeng on 2017/5/4.
 * Email:496349136@qq.com
 */

public class MadUIConfig {
    private static ContextProvider contextProvider;

    public static void init(final Application context) {
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
