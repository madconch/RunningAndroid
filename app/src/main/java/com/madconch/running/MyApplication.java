package com.madconch.running;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/6/16.
 * Email:496349136@qq.com
 */

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        Class<?> activityThreadClass = null;
        try {
            //加载activity thread的class
            activityThreadClass = Class.forName("android.app.ActivityThread", false, getClassLoader());

            //找到方法currentActivityThread
            Method method = activityThreadClass.getDeclaredMethod("currentActivityThread");
            //由于这个方法是静态的，所以传入Null就行了
            Object currentActivityThread = method.invoke(null);

            //把之前ActivityThread中的mInstrumentation替换成我们自己的
            Field field = activityThreadClass.getDeclaredField("mInstrumentation");
            field.setAccessible(true);
            Instrumentation instrumentation = (Instrumentation) field.get(currentActivityThread);
            InstrumentProxy instrumentationProxy = new InstrumentProxy(instrumentation);
            field.set(currentActivityThread, instrumentationProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
