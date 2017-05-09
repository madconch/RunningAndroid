package com.madconch.running.base.widget.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madconch.running.base.R;
import com.madconch.running.base.helper.paging.IRefreshProvider;
import com.madconch.running.ui.refresh.MadRefreshLayout;

/**
 * 功能描述:分页Activity
 * Created by LuoHaifeng on 2017/5/8.
 * Email:496349136@qq.com
 */

public abstract class BaseRefreshActivity extends BaseActivity implements IRefreshProvider {
    MadRefreshLayout refreshLayout;

    protected abstract View provideRefreshContentLayout(MadRefreshLayout refreshLayout);

    @Override
    protected View provideContentView(ViewGroup container) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_paging, container, false);
        refreshLayout = (MadRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.addView(provideRefreshContentLayout(refreshLayout));
        return refreshLayout;
    }

    @Override
    public MadRefreshLayout provideRefreshLayout() {
        return refreshLayout;
    }
}
