package com.madconch.running.mvcdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madconch.running.base.widget.activity.BaseActivity;

public class NavigationActivity extends BaseActivity {
    @Override
    protected View provideContentView(ViewGroup container) {
        return LayoutInflater.from(getContext()).inflate(R.layout.activity_navigation, container, false);
    }
}
