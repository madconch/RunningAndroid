package com.madconch.running.base.paging;

import android.support.annotation.NonNull;

import com.madconch.running.base.common.RetryListener;
import com.madconch.running.base.common.SimpleSubscriber;
import com.madconch.running.base.common.TransformerProvider;
import com.madconch.running.ui.loading.ILoadingHelper;
import com.madconch.running.uiconfig.refresh.IRefreshLayout;

import rx.Observable;

/**
 * 功能描述:分页帮助工具
 * Created by LuoHaifeng on 2017/4/21.
 * Email:496349136@qq.com
 */

public class MadPagingHelper<T> implements IRefreshLayout.OnRefreshLoadMoreListener{
    public interface IPagingCallback<T>{
        Observable<T> onRequestData(int pageIndex, int pageSize);
        void onBindData(int pageIndex, int pageSize, T data);
    }

    private int startPageNumber = 1;
    private int curPageIndex = startPageNumber - 1;
    private int pageSize = 15;
    private boolean haveMoreData = true;

    private boolean haveData = false;
    private IRefreshLayout refreshLayout;
    private ILifeCycleProvider lifeCycleProvider;
    private ILoadingHelper loadingHelper;
    private IPagingCallback<T> pagingCallback;

    private IPagingProvider pagingProvider = new IPagingProvider() {
        @Override
        public boolean haveMoreData() {
            return haveMoreData;
        }

        @Override
        public IRefreshLayout getRefreshLayout() {
            return refreshLayout;
        }

        @Override
        public boolean haveData() {
            return haveData;
        }

        @Override
        public ILoadingHelper provideLoadingHelper() {
            return loadingHelper;
        }
    };

    public void setPagingCallback(IPagingCallback<T> pagingCallback) {
        this.pagingCallback = pagingCallback;
    }

    private RetryListener refreshRetryListener = new RetryListener() {
        @Override
        public void onRetry(ILoadingHelper loadingHelper) {
            startLoading();
        }
    };

    public MadPagingHelper(@NonNull IRefreshLayout refreshLayout, @NonNull ILifeCycleProvider lifeCycleProvider, @NonNull ILoadingHelper loadingHelper) {
        this.refreshLayout = refreshLayout;
        this.lifeCycleProvider = lifeCycleProvider;
        this.loadingHelper = loadingHelper;
        this.refreshLayout.setRefreshLoadMoreListener(this);
    }

    public MadPagingHelper(@NonNull IRefreshLayout refreshLayout, @NonNull ILifeCycleProvider lifeCycleProvider, @NonNull ILoadingHelper loadingHelper, int startPageNumber, int pageSize) {
        this(refreshLayout,lifeCycleProvider,loadingHelper);
        this.startPageNumber = startPageNumber;
        this.pageSize = pageSize;
    }

    public void startRefresh(){
        refreshLayout.startRefresh();
    }

    public void startLoading(){
        requestData(startPageNumber,pageSize);
    }

    @Override
    public void onRefresh() {
        requestData(startPageNumber, pageSize);
    }

    @Override
    public void onLoadMore() {
        requestData(curPageIndex + 1, pageSize);
    }

    protected void requestData(final int pageIndex, final int pageSize){
        pagingCallback.onRequestData(pageIndex,pageSize)
                .compose(TransformerProvider.<T>providePagingTransformer(lifeCycleProvider,pagingProvider,refreshRetryListener))
                .subscribe(new SimpleSubscriber<T>(){
                    @Override
                    public void onNext(T t) {
                        super.onNext(t);
                        haveData = true;
                        curPageIndex = pageIndex;
                        pagingCallback.onBindData(pageIndex,pageSize,t);
                    }
                });
    }

    public void setHaveMoreData(boolean haveMoreData) {
        this.haveMoreData = haveMoreData;
    }

    public void setHaveData(boolean haveData) {
        this.haveData = haveData;
    }

    public boolean isHaveMoreData() {
        return haveMoreData;
    }

    public boolean isHaveData() {
        return haveData;
    }

    public int getStartPageNumber() {
        return startPageNumber;
    }

    public int getCurPageIndex() {
        return curPageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public IRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public ILifeCycleProvider getLifeCycleProvider() {
        return lifeCycleProvider;
    }

    public ILoadingHelper getLoadingHelper() {
        return loadingHelper;
    }
}
