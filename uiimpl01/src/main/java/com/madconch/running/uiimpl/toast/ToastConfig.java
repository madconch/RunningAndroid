package com.madconch.running.uiimpl.toast;

import android.content.Context;
import android.widget.Toast;

import com.madconch.running.uiconfig.toast.IToastConfig;

/**
 * 功能描述:关于Toast的样式定义
 * Created by LuoHaifeng on 2017/5/4.
 * Email:496349136@qq.com
 */

public class ToastConfig implements IToastConfig {
    @Override
    public void info(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void warn(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void error(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
