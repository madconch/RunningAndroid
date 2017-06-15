package com.madconch.running.mvcdemo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.madconch.running.base.widget.activity.BaseActivity;
import com.madconch.running.ui.refresh.MadRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TestActivity extends BaseActivity {
    @BindView(R.id.rv_content)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    MadRefreshLayout refreshLayout;
    @Override
    protected View provideContentView(ViewGroup container) {
        return LayoutInflater.from(getContext()).inflate(R.layout.activity_test, container, false);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(new MyAdapter());
    }

    private class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        private List<String> datas = new ArrayList<>();

        public MyAdapter() {
            for (int i = 0; i < 100; i++) {
                datas.add("Item " + i);
            }
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            holder.textView.setText(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

    private class MyHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
