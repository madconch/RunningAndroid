package com.madconch.running.uiimpl.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;

import com.madconch.running.uiconfig.config.MadUIConfig;
import com.madconch.running.uiconfig.toolbar.ITitleBarConfig;
import com.madconch.running.uiimpl.R;

/**
 * 功能描述:标题栏样式配置
 * Created by LuoHaifeng on 2017/5/5.
 * Email:496349136@qq.com
 */

public class TitleBarConfig implements ITitleBarConfig {
    @Override
    public int provideBackButtonImageResource(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.UIConfigStyle);
        int backButton = typedArray.getResourceId(R.styleable.UIConfigStyle_uiTitleBarBackButtonResource, R.mipmap.ic_back);
        typedArray.recycle();
        return backButton;
    }

    @Override
    public int provideTitleBarBackgroundColor(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.UIConfigStyle);
        int themeColor = typedArray.getColor(R.styleable.UIConfigStyle_uiTitleBarBackgroundColor, context.getResources().getColor(R.color.theme_color));
        typedArray.recycle();
        return themeColor;
    }

    @Override
    public int provideMenuSpacing() {
        return MadUIConfig.getContextProvider().provideContext().getResources().getDimensionPixelSize(R.dimen.title_bar_button_padding);
    }

    @Override
    public int provideTitleStyle() {
        return R.style.Theme_ToolBar_Base_Title;
    }

    @Override
    public int provideMenuStyle() {
        return R.style.Theme_ToolBar_Base_Menu;
    }

    @Override
    public boolean backButtonIsTint() {
        return true;
    }

    @Override
    public int provideBackButtonTintColor(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.UIConfigStyle);
        int themeColor = typedArray.getColor(R.styleable.UIConfigStyle_uiTitleBarTitleTextColor, Color.WHITE);
        typedArray.recycle();
        return themeColor;
    }
}
