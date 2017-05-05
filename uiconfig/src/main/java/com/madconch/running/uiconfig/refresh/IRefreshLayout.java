package com.madconch.running.uiconfig.refresh;

/**
 * 刷新组件的基本功能
 * Created by LuoHaifeng on 2017/3/9.
 */

public interface IRefreshLayout {
    void startRefresh();
    void setRefreshEnable(boolean enable);
    void setLoadMoreEnable(boolean enable);
    void refreshCompleted();
    void loadMoreCompleted();
    void setRefreshLoadMoreListener(OnRefreshLoadMoreListener listener);

    interface OnRefreshLoadMoreListener{
        void onRefresh();
        void onLoadMore();
    }
}
