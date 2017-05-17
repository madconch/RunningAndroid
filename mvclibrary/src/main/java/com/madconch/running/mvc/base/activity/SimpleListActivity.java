package com.madconch.running.mvc.base.activity;

import android.os.Bundle;

import com.madconch.running.base.helper.paging.MadPagingListHelper;
import com.madconch.running.base.widget.activity.BaseListActivity;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/8.
 * Email:496349136@qq.com
 */

public abstract class SimpleListActivity<T> extends BaseListActivity implements MadPagingListHelper.IPagingListCallback<T> {
    MadPagingListHelper<T> pagingListHelper;

    @Override
    protected void internalInit(Bundle savedInstanceState) {
        super.internalInit(savedInstanceState);
        pagingListHelper = providePagingListHelper();
    }

    @Override
    protected void onReady() {
        super.onReady();
        pagingListHelper.startLoading();
    }

    protected MadPagingListHelper<T> providePagingListHelper() {
        MadPagingListHelper<T> pagingListHelper = new MadPagingListHelper<>(provideRefreshLayout(), getRecyclerView(), this, provideLoadingHelper());
        pagingListHelper.setPagingListCallback(this);
        return pagingListHelper;
    }

    @Override
    public boolean haveData() {
        return pagingListHelper.getAdapter().getItemCount() > 0;
    }
}
