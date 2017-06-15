package com.madconch.running.mvcdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madconch.running.base.widget.adapter.ViewHolder;
import com.madconch.running.mvc.base.fragment.SimpleListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/6/15.
 * Email:496349136@qq.com
 */

public class TestSingleFragment extends SimpleListFragment<String> {
    List<String> datas = new ArrayList<>();

    @Override
    protected void init(View root, @Nullable Bundle savedInstanceState) {
        for (int i = 1; i <= 50; i++) {
            datas.add("This is Item " + i);
        }
    }

    @Override
    public View provideItemLayout(ViewGroup parent, int viewType) {
        return LayoutInflater.from(getContext()).inflate(R.layout.item_layout_simple_list, parent, false);
    }

    @Override
    public void onBindItemData(int position, ViewHolder holder, String data) {
        holder.setText(R.id.tv_content, data);
    }

    @Override
    public Observable<List<String>> onRequestData(int pageIndex, int pageSize) {
        int startIndex = (pageIndex - 1) * pageSize;
        if (startIndex >= datas.size()) {
            List<String> emptyDatas = new ArrayList<>();
            return Observable.just(emptyDatas).delay(500, TimeUnit.MILLISECONDS);
        } else {
            int endIndex = startIndex + pageSize;
            endIndex = Math.min(endIndex, datas.size());
            return Observable.just(datas.subList(startIndex, endIndex)).delay(500, TimeUnit.MILLISECONDS);
        }
    }
}
