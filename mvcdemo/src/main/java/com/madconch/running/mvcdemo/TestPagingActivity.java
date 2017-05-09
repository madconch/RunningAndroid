package com.madconch.running.mvcdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.madconch.running.mvc.base.activity.PagingActivity;
import com.madconch.running.ui.refresh.MadRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/8.
 * Email:496349136@qq.com
 */

public class TestPagingActivity extends PagingActivity<String> {
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
    public Observable<String> onRequestData(final int pageIndex, int pageSize) {
        return Observable.just(datas.get(pageIndex - 1))
                .delay(500, TimeUnit.MILLISECONDS)
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (pageIndex == datas.size()) {
                            getPagingHelper().setHaveMoreData(false);
                        }
                    }
                });
    }

    @Override
    public void onBindData(int pageIndex, int pageSize, String data) {
        tvContent.setText(data);
    }
}
