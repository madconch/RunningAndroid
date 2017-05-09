package com.madconch.running.base.widget.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.madconch.running.base.R;
import com.madconch.running.ui.refresh.MadRefreshLayout;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/8.
 * Email:496349136@qq.com
 */

public abstract class BaseSimpleListFragment extends BaseRefreshFragment {
    RecyclerView recyclerView;

    @Override
    protected View provideRefreshContentLayout(MadRefreshLayout refreshLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_simple_list, refreshLayout, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        return view;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}
