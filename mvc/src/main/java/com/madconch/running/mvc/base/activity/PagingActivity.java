package com.madconch.running.mvc.base.activity;

import android.os.Bundle;

import com.madconch.running.base.helper.paging.MadPagingHelper;
import com.madconch.running.base.widget.activity.BaseRefreshActivity;

/**
 * 功能描述:分页Activity
 * Created by LuoHaifeng on 2017/5/8.
 * Email:496349136@qq.com
 */

public abstract class PagingActivity<T> extends BaseRefreshActivity implements MadPagingHelper.IPagingCallback<T> {
    MadPagingHelper<T> pagingHelper;

    @Override
    protected void internalInit(Bundle savedInstanceState) {
        super.internalInit(savedInstanceState);
        pagingHelper = providePagingHelper();
    }

    @Override
    protected void onReady() {
        super.onReady();
        pagingHelper.startLoading();
    }

    protected MadPagingHelper<T> providePagingHelper() {
        MadPagingHelper<T> pagingHelper = new MadPagingHelper<>(provideRefreshLayout(), this, provideLoadingHelper());
        pagingHelper.setPagingCallback(this);
        return pagingHelper;
    }

    public MadPagingHelper<T> getPagingHelper() {
        return pagingHelper;
    }

    @Override
    public boolean haveData() {
        return pagingHelper.isHaveData();
    }
}
