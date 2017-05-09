package com.madconch.running.mvc.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.madconch.running.base.helper.paging.MadRefreshHelper;
import com.madconch.running.base.widget.fragment.BaseRefreshFragment;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/8.
 * Email:496349136@qq.com
 */

public abstract class RefreshFragment<T> extends BaseRefreshFragment implements MadRefreshHelper.RefreshCallback<T> {
    MadRefreshHelper<T> refreshHelper;

    @Override
    protected void internalInit(View root, @Nullable Bundle savedInstanceState) {
        super.internalInit(root, savedInstanceState);
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
