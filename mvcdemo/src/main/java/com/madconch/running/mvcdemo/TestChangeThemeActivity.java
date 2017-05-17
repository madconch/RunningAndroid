package com.madconch.running.mvcdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madconch.running.base.widget.activity.BaseActivity;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/17.
 * Email:496349136@qq.com
 */

public class TestChangeThemeActivity extends BaseActivity {
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getTitleBar().addRightTextButton(R.string.complete);
        showBackButton();
        //在AndroidManifest文件里面配置的主题
        setTitle("主题切换Demo");
    }

    @Override
    protected View provideContentView(ViewGroup container) {
        return LayoutInflater.from(getContext()).inflate(R.layout.activity_paging, container, false);
    }
}
