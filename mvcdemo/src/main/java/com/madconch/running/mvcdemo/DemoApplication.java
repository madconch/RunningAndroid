package com.madconch.running.mvcdemo;

import android.app.Application;

import com.madconch.running.mvc.common.MVCApplication;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/8.
 * Email:496349136@qq.com
 */

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MVCApplication.onCreateInit(this);
    }
}
