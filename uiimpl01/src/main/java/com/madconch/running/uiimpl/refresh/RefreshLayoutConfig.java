package com.madconch.running.uiimpl.refresh;

import android.content.Context;
import android.view.ViewGroup;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.madconch.running.uiconfig.refresh.IRefreshLayout;
import com.madconch.running.uiconfig.refresh.IRefreshLayoutConfig;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/3.
 * Email:496349136@qq.com
 */

public class RefreshLayoutConfig implements IRefreshLayoutConfig {
    @Override
    public void initRefreshLayout(ViewGroup refreshLayout) {
        FixTwinkRefreshLayout layout = (FixTwinkRefreshLayout) refreshLayout;
//        layout.onInflateFinish();
        layout.setEnableOverScroll(false);
        layout.setHeaderView(new SinaRefreshView(layout.getContext()));
        layout.setBottomView(new LoadingView(layout.getContext()));
        layout.proxyOnFinishInflate();
    }

    @Override
    public ViewGroup provideRealRefreshLayout(Context context) {
        return new FixTwinkRefreshLayout(context);
    }

    @Override
    public void startRefresh(ViewGroup refreshLayout) {
        ((TwinklingRefreshLayout) refreshLayout).startRefresh();
    }

    @Override
    public void setRefreshEnable(ViewGroup refreshLayout, boolean enable) {
        ((TwinklingRefreshLayout) refreshLayout).setEnableRefresh(enable);
    }

    @Override
    public void setLoadMoreEnable(ViewGroup refreshLayout, boolean enable) {
        ((TwinklingRefreshLayout) refreshLayout).setEnableLoadmore(enable);
    }

    @Override
    public void refreshCompleted(ViewGroup refreshLayout) {
        ((TwinklingRefreshLayout) refreshLayout).finishRefreshing();
    }

    @Override
    public void loadMoreCompleted(ViewGroup refreshLayout) {
        ((TwinklingRefreshLayout) refreshLayout).finishLoadmore();
    }

    @Override
    public void setRefreshLoadMoreListener(ViewGroup refreshLayout, final IRefreshLayout.OnRefreshLoadMoreListener listener) {
        ((TwinklingRefreshLayout) refreshLayout).setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (listener != null) {
                    listener.onRefresh();
                }
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (listener != null) {
                    listener.onLoadMore();
                }
            }
        });
    }
}
