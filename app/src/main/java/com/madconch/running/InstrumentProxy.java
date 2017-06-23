package com.madconch.running;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.Field;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/6/16.
 * Email:496349136@qq.com
 */

public class InstrumentProxy extends Instrumentation {
    public InstrumentProxy(Instrumentation realInstrumentation) {
        Field[] fields = Instrumentation.class.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(realInstrumentation);
                    field.set(this, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        System.out.println(">>>new Application before...");
        return super.newApplication(cl, className, context);
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        System.out.println(">>>new Activity before...:" + className);
        return super.newActivity(cl, className, intent);
    }
}
