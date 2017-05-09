package com.madconch.running.uiconfig.toolbar;

import android.content.Context;

/**
 * 功能描述:自定义TitleBar接口
 * Created by LuoHaifeng on 2017/5/5.
 * Email:496349136@qq.com
 */

public interface ITitlelbarConfig {
    int provideBackButtonImageResource();

    int provideTitleBarBackgroundColor(Context context);
    /***
     * 提供菜单之间的间距 px
     */
    int provideMenuSpacing();

    int provideTitleStyle();

    int provideMenuStyle();
}
