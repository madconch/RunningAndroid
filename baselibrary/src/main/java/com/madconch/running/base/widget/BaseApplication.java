package com.madconch.running.base.widget;

import android.app.Application;

import com.madconch.running.base.config.MadBaseConfig;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/8.
 * Email:496349136@qq.com
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        onCreateInit(this);
    }

    public static void onCreateInit(Application application) {
        MadBaseConfig.init(application);
    }
}
