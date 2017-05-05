package com.madconch.running.uiconfig.refresh;

import android.content.Context;
import android.view.ViewGroup;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/3.
 * Email:496349136@qq.com
 */

public interface IRefreshLayoutConfig {
    void initRefreshLayout(ViewGroup refreshLayout);
    ViewGroup provideRealRefreshLayout(Context context);

    void startRefresh(ViewGroup refreshLayout);

    void setRefreshEnable(ViewGroup refreshLayout, boolean enable);

    void setLoadMoreEnable(ViewGroup refreshLayout, boolean enable);

    void refreshCompleted(ViewGroup refreshLayout);

    void loadMoreCompleted(ViewGroup refreshLayout);

    void setRefreshLoadMoreListener(ViewGroup refreshLayout, IRefreshLayout.OnRefreshLoadMoreListener listener);
}
