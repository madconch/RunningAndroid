package com.madconch.running.mvcdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.madconch.running.base.widget.activity.BaseActivity;
import com.madconch.running.base.widget.activity.WebViewActivity;
import com.madconch.running.gallery.PictureManagerActivity;
import com.madconch.running.ui.toast.MadToast;
import com.madconch.running.utillibrary.MadSystemUtil;
import com.madconch.running.utillibrary.savesate.SaveState;

import java.io.File;

import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {
    @SaveState(key = "xx")
    private String data;
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

    @OnClick({R.id.btn_01, R.id.btn_02, R.id.btn_03, R.id.btn_04, R.id.btn_05, R.id.btn_06, R.id.btn_07, R.id.btn_08, R.id.btn_09,R.id.btn_10,R.id.btn_11})
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
                break;
            case R.id.btn_06:
                MadSystemUtil.requestCall(this, "122232131")
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(@NonNull Object o) throws Exception {
                                MadToast.info("拨打成功");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                MadToast.error("拨打失败");
                            }
                        });
                break;
            case R.id.btn_07:
                MadSystemUtil.requestInstallApk(this, new File(Environment.getExternalStorageDirectory(), "a.apk").getAbsolutePath(), "com.madconch.running.mvcdemo.fileProvider")
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(@NonNull Object o) throws Exception {
                                MadToast.info("安装成功");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                MadToast.error("安装失败");
                            }
                        });
                break;
            case R.id.btn_08:
                MadSystemUtil.requestTakePhoto(this, "com.madconch.running.mvcdemo.fileProvider")
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(@NonNull String s) throws Exception {
                                MadToast.error("拍照成功" + s);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                MadToast.error("请求失败");
                            }
                        });
                break;
            case R.id.btn_09:
                startActivity(new Intent(this, RequestProgressActivity.class));
                break;
            case R.id.btn_10:
                MadToast.warn("暂未开通");
//                startActivity(new Intent(this, NavigationActivity.class));
                break;
            case R.id.btn_11:
                startActivity(WebViewActivity.newIntent(this,"百度一下","http://www.baidu.com",false));
                break;
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
