package com.madconch.running.mvcdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.madconch.running.mvc.base.activity.RefreshActivity;
import com.madconch.running.ui.refresh.MadRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/8.
 * Email:496349136@qq.com
 */

public class TestRefreshActivity extends RefreshActivity<String> {
    List<String> datas = new ArrayList<>();

    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        for (int i = 1; i <= 10; i++) {
            datas.add("This is Item " + i);
        }
        showBackButton();
    }

    @Override
    protected View provideRefreshContentLayout(MadRefreshLayout refreshLayout) {
        return LayoutInflater.from(getContext()).inflate(R.layout.activity_paging, refreshLayout, false);
    }

    @Override
    public Observable<String> onRequestData() {
        int randomIndex = new Random().nextInt(datas.size());
        return Observable.just(datas.get(randomIndex))
                .delay(500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onBindData(String data) {
        tvContent.setText(data);
    }
}
