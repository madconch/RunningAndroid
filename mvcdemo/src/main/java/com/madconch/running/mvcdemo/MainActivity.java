package com.madconch.running.mvcdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madconch.running.base.widget.activity.BaseActivity;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @Override
    protected View provideContentView(ViewGroup container) {
        return LayoutInflater.from(getContext()).inflate(R.layout.activity_main, container, false);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        findViewById(R.id.btn_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TestPagingActivity.class));
            }
        });
    }

    @Override
    public boolean haveData() {
        return false;
    }

    @OnClick({R.id.btn_01, R.id.btn_02, R.id.btn_03})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_01:
                startActivity(new Intent(this, TestPagingActivity.class));
                break;
            case R.id.btn_02:
                startActivity(new Intent(this, TestRefreshActivity.class));
                break;
            case R.id.btn_03:
                startActivity(new Intent(this, TestSimpleListActivity.class));
                break;
            default:
                break;
        }
    }
}
