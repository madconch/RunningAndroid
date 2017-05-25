package com.madconch.running.base.widget;

import android.app.Application;

import com.madconch.running.base.config.MadBaseConfig;
import com.tencent.smtt.sdk.QbSdk;

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
        QbSdk.initX5Environment(application.getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                System.out.println(">>>onCoreInitFinished");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                System.out.println(">>>onViewInitFinished:" + b);
            }
        });
    }
}
