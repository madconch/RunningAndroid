package com.madconch.running.mvc.common;

import android.app.Application;

import com.madconch.running.base.widget.BaseApplication;

/**
 * 功能描述:mvc框架的Application
 * Created by LuoHaifeng on 2017/5/8.
 * Email:496349136@qq.com
 */

public class MVCApplication extends BaseApplication {
    public static void onCreateInit(Application application) {
        BaseApplication.onCreateInit(application);
    }
}
