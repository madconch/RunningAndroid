package com.madconch.running.base.widget.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madconch.running.base.R;
import com.madconch.running.utillibrary.savesate.SaveState;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/6/15.
 * Email:496349136@qq.com
 */

public class SingeFragmentActivity extends BaseActivity {
    Fragment fragment;
    @SaveState(key = "fragmentClassName")
    String fragmentClassName;
    @SaveState(key = "fragmentParams")
    Bundle fragmentParams;

    @Override
    protected View provideContentView(ViewGroup container) {
        return LayoutInflater.from(getContext()).inflate(R.layout.activity_simple_fragment, container, false);
    }

    public static Intent newIntent(Context context, Class<? extends Fragment> fragmentCls, Bundle fragmentParams) {
        return newIntent(context, SingeFragmentActivity.class, fragmentCls, fragmentParams);
    }

    public static Intent newIntent(Context context, Class<? extends SingeFragmentActivity> activityCls, Class<? extends Fragment> fragmentCls, Bundle fragmentParams) {
        Intent intent = new Intent(context, activityCls);
        intent.putExtra("fragmentClassName", fragmentCls.getName());
        intent.putExtra("fragmentParams", fragmentParams);
        return intent;
    }

    @Override
    protected void internalInit(Bundle savedInstanceState) {
        super.internalInit(savedInstanceState);
        try {
            fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
            fragment.setArguments(fragmentParams);
            getSupportFragmentManager().beginTransaction().add(R.id.base_fl_container, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
