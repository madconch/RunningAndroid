package com.madconch.running.base.helper.paging;

import android.support.annotation.NonNull;

import com.madconch.running.ui.loading.ILoadingHelper;
import com.madconch.running.uiconfig.refresh.IRefreshLayout;

import io.reactivex.Observable;

/**
 * 功能描述:提供带刷新的场景辅助类
 * Created by LuoHaifeng on 2017/4/21.
 * Email:496349136@qq.com
 */

public class MadRefreshHelper<T> extends MadPagingHelper<T> implements MadPagingHelper.IPagingCallback<T> {
    public interface RefreshCallback<T> {
        Observable<T> onRequestData();

        void onBindData(T data);
    }

    private RefreshCallback<T> refreshCallback;

    public RefreshCallback<T> getRefreshCallback() {
        return refreshCallback;
    }

    public void setRefreshCallback(RefreshCallback<T> refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    public MadRefreshHelper(@NonNull IRefreshLayout refreshLayout, @NonNull ILifeCycleProvider lifeCycleProvider, @NonNull ILoadingHelper loadingHelper) {
        super(refreshLayout, lifeCycleProvider, loadingHelper);
        init();
    }

    public MadRefreshHelper(@NonNull IRefreshLayout refreshLayout, @NonNull ILifeCycleProvider lifeCycleProvider, @NonNull ILoadingHelper loadingHelper, int startPageNumber, int pageSize) {
        super(refreshLayout, lifeCycleProvider, loadingHelper, startPageNumber, pageSize);
        init();
    }

    private void init() {
        setPagingCallback(this);
        getRefreshLayout().setLoadMoreEnable(false);
    }

    @Override
    public final Observable<T> onRequestData(int pageIndex, int pageSize) {
        return refreshCallback.onRequestData();
    }

    @Override
    public final void onBindData(int pageIndex, int pageSize, T data) {
        setHaveData(data != null);
        setHaveMoreData(false);
        refreshCallback.onBindData(data);
    }
}
