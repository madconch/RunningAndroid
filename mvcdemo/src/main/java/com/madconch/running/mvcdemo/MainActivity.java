package com.madconch.running.mvcdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.madconch.running.base.widget.activity.BaseActivity;
import com.madconch.running.gallery.PictureManagerActivity;

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
//        getTitleBar().instanceTextView(getTitleBar().getRightButtonContainer()).setText("查看");
//        getTitleBar().instanceTextView(getTitleBar().getRightButtonContainer()).setText("取消");
    }

    @Override
    public boolean haveData() {
        return false;
    }

    @OnClick({R.id.btn_01, R.id.btn_02, R.id.btn_03, R.id.btn_04, R.id.btn_05})
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
            case R.id.btn_04:
                startActivity(new Intent(this, PictureManagerActivity.class));
                break;
            case R.id.btn_05:
                startActivity(new Intent(this, TestChangeThemeActivity.class));
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main,menu);
//        MenuItem menuItem = menu.findItem(R.id.menu_item_add);
//        AppCompatButton button = (AppCompatButton) menuItem.getActionView().findViewById(R.id.test_button);
//        button.setText("new");
        return super.onCreateOptionsMenu(menu);
    }
}
