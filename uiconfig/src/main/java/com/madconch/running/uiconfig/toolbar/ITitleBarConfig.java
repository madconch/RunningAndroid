package com.madconch.running.uiconfig.toolbar;

import android.content.Context;

/**
 * 功能描述:自定义TitleBar接口
 * Created by LuoHaifeng on 2017/5/5.
 * Email:496349136@qq.com
 */

public interface ITitleBarConfig {
    int provideBackButtonImageResource(Context context);

    int provideTitleBarBackgroundColor(Context context);

    /***
     * 提供菜单之间的间距 px
     */
    int provideMenuSpacing();

    int provideTitleStyle();

    int provideMenuStyle();

    /***
     * 返回按钮是否跟随主题
     */
    boolean backButtonIsTint();

    /***
     * 返回按钮跟随的主题颜色
     */
    int provideBackButtonTintColor(Context context);
}
