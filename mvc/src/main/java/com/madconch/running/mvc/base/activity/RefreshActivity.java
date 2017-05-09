package com.madconch.running.mvc.base.activity;

import android.os.Bundle;

import com.madconch.running.base.helper.paging.MadRefreshHelper;
import com.madconch.running.base.widget.activity.BaseRefreshActivity;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/8.
 * Email:496349136@qq.com
 */

public abstract class RefreshActivity<T> extends BaseRefreshActivity implements MadRefreshHelper.RefreshCallback<T> {
    MadRefreshHelper<T> refreshHelper;

    @Override
    protected void internalInit(Bundle savedInstanceState) {
        super.internalInit(savedInstanceState);
        refreshHelper = provideRefreshHelper();
    }

    @Override
    protected void onReady() {
        super.onReady();
        refreshHelper.startLoading();
    }

    protected MadRefreshHelper<T> provideRefreshHelper() {
        MadRefreshHelper<T> refreshHelper = new MadRefreshHelper<>(provideRefreshLayout(), this, provideLoadingHelper());
        refreshHelper.setRefreshCallback(this);
        return refreshHelper;
    }

    public MadRefreshHelper<T> getRefreshHelper() {
        return refreshHelper;
    }

    @Override
    public boolean haveData() {
        return refreshHelper.isHaveData();
    }
}
