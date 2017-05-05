package com.madconch.running.ui.refresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.madconch.running.uiconfig.refresh.IRefreshLayout;
import com.madconch.running.uiconfig.refresh.IRefreshLayoutConfig;
import com.madconch.running.uiimpl.refresh.RefreshLayoutConfig;

/**
 * 功能描述:上下拉刷新布局
 * Created by LuoHaifeng on 2017/5/3.
 * Email:496349136@qq.com
 */

public class MadRefreshLayout extends LinearLayout implements IRefreshLayout {
    private IRefreshLayoutConfig config;
    private ViewGroup realRefreshLayout;

    public MadRefreshLayout(Context context) {
        super(context);
        init();
    }

    public MadRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MadRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        config = new RefreshLayoutConfig();
        realRefreshLayout = config.provideRealRefreshLayout(getContext());
        post(new Runnable() {
            @Override
            public void run() {
                if (getChildCount() > 0) {
                    View view = getChildAt(0);
                    removeAllViews();
                    realRefreshLayout.addView(view);
                    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    addView(realRefreshLayout, lp);
                    config.initRefreshLayout(realRefreshLayout);
                }
            }
        });
    }

    @Override
    public void startRefresh() {
        config.startRefresh(realRefreshLayout);
    }

    @Override
    public void setRefreshEnable(boolean enable) {
        config.setRefreshEnable(realRefreshLayout, enable);
    }

    @Override
    public void setLoadMoreEnable(boolean enable) {
        config.setLoadMoreEnable(realRefreshLayout, enable);
    }

    @Override
    public void refreshCompleted() {
        config.refreshCompleted(realRefreshLayout);
    }

    @Override
    public void loadMoreCompleted() {
        config.loadMoreCompleted(realRefreshLayout);
    }

    @Override
    public void setRefreshLoadMoreListener(OnRefreshLoadMoreListener listener) {
        config.setRefreshLoadMoreListener(realRefreshLayout, listener);
    }
}
