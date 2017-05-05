package com.madconch.running.base.helper.paging;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.madconch.running.base.common.BaseAdapter;
import com.madconch.running.base.common.ViewHolder;
import com.madconch.running.ui.loading.ILoadingHelper;
import com.madconch.running.uiconfig.refresh.IRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * 功能描述:分页列表帮助类
 * Created by LuoHaifeng on 2017/4/21.
 * Email:496349136@qq.com
 */

public class MadPagingListHelper<T> extends MadPagingHelper<List<T>> implements MadPagingHelper.IPagingCallback<List<T>> {
    public interface IPagingListCallback<T> {
        View provideItemLayout(ViewGroup parent, int viewType);

        void onBindItemData(int position, ViewHolder holder, T data);

        Observable<List<T>> onRequestData(int pageIndex, int pageSize);
    }

    private RecyclerView recyclerView;
    private IPagingListCallback<T> pagingListCallback;

    public IPagingListCallback<T> getPagingListCallback() {
        return pagingListCallback;
    }

    public void setPagingListCallback(IPagingListCallback<T> pagingListCallback) {
        this.pagingListCallback = pagingListCallback;
    }

    private BaseAdapter<T, ViewHolder> adapter = new BaseAdapter<T, ViewHolder>() {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(parent.getContext(), pagingListCallback.provideItemLayout(parent, viewType));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            pagingListCallback.onBindItemData(position, holder, getDatas().get(position));
        }

        @Override
        public int getItemViewType(int position) {
            return provideItemViewType(position, getDatas().get(position));
        }
    };

    public MadPagingListHelper(@NonNull IRefreshLayout refreshLayout, @NonNull RecyclerView recyclerView, @NonNull ILifeCycleProvider lifeCycleProvider, @NonNull ILoadingHelper loadingHelper) {
        super(refreshLayout, lifeCycleProvider, loadingHelper);
        this.recyclerView = recyclerView;
        init();
    }

    public MadPagingListHelper(@NonNull IRefreshLayout refreshLayout, @NonNull RecyclerView recyclerView, @NonNull ILifeCycleProvider lifeCycleProvider, @NonNull ILoadingHelper loadingHelper, int startPageNumber, int pageSize) {
        super(refreshLayout, lifeCycleProvider, loadingHelper, startPageNumber, pageSize);
        this.recyclerView = recyclerView;
        init();
    }

    private void init() {
        initRecyclerView(recyclerView);
        setPagingCallback(this);
    }

    protected void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public Observable<List<T>> onRequestData(int pageIndex, int pageSize) {
        return pagingListCallback.onRequestData(pageIndex, pageSize);
    }

    @Override
    public void onBindData(int pageIndex, int pageSize, List<T> data) {
        if (data == null) {
            data = new ArrayList<>();
        }

        if (pageIndex == getStartPageNumber()) {//首页
            adapter.getDatas().clear();
            adapter.getDatas().addAll(data);
            adapter.notifyDataSetChanged();
        } else {
            int insertIndex = adapter.getDatas().size();
            adapter.getDatas().addAll(data);
            adapter.notifyItemRangeInserted(insertIndex, data.size());
        }

        setHaveData(adapter.getItemCount() > 0);
        setHaveMoreData(data.size() == getPageSize());
    }

    public int provideItemViewType(int position, T data) {
        return 0;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public BaseAdapter<T, ViewHolder> getAdapter() {
        return adapter;
    }
}
