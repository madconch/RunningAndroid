package com.madconch.running.uiimpl.loading;

import com.madconch.running.uiconfig.loading.ILoadingHelperConfig;
import com.madconch.running.uiimpl.R;

/**
 * 功能描述:提供加载状态样式
 * Created by LuoHaifeng on 2017/5/3.
 * Email:496349136@qq.com
 */

public class LoadingHelperConfig implements ILoadingHelperConfig {
    @Override
    public int provideEmptyLayoutId() {
        return R.layout.layout_loading_state_load_no_data;
    }

    @Override
    public int provideLoadingLayoutId() {
        return R.layout.layout_loading_state_loading;
    }

    @Override
    public int provideNoNetworkLayoutId() {
        return R.layout.layout_loading_state_no_network;
    }

    @Override
    public int provideErrorLayoutId() {
        return R.layout.layout_loading_state_load_failed;
    }

    @Override
    public int provideNoLoginLayoutId() {
        return R.layout.layout_loading_state_load_need_login;
    }

    @Override
    public int provideIconViewId() {
        return R.id.iv_icon;
    }

    @Override
    public int provideMessageViewId() {
        return R.id.tv_message;
    }

    @Override
    public int provideRefreshBtnViewId() {
        return R.id.btn_refresh;
    }

    @Override
    public int provideRetryBtnViewId() {
        return R.id.btn_retry;
    }

    @Override
    public int provideLoginBtnViewId() {
        return R.id.btn_login;
    }

    @Override
    public int provideSettingBtnViewId() {
        return R.id.btn_setting;
    }
}
