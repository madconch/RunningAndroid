package com.madconch.running.uiimpl.toolbar;

import com.madconch.running.uiconfig.config.MadUIConfig;
import com.madconch.running.uiconfig.toolbar.ITitlelbarConfig;
import com.madconch.running.uiimpl.R;

/**
 * 功能描述:标题栏样式配置
 * Created by LuoHaifeng on 2017/5/5.
 * Email:496349136@qq.com
 */

public class TitleBarConfig implements ITitlelbarConfig {
    @Override
    public int provideBackButtonImageResource() {
        return R.mipmap.ic_back;
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
}
