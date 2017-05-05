package com.madconch.running.uiconfig.toast;

import android.content.Context;

/**
 * 功能描述:Toast的样式定义接口
 * Created by LuoHaifeng on 2017/5/4.
 * Email:496349136@qq.com
 */

public interface IToastConfig {
    void info(Context context, String message);

    void warn(Context context, String message);

    void error(Context context, String message);
}
