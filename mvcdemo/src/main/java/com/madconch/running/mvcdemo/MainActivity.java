package com.madconch.running.mvcdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.madconch.running.base.common.TransformerProvider;
import com.madconch.running.base.helper.update.IUpdateEntity;
import com.madconch.running.base.helper.update.UpdateManager;
import com.madconch.running.base.helper.update.UpdateResult;
import com.madconch.running.base.widget.activity.BaseActivity;
import com.madconch.running.base.widget.activity.SingeFragmentActivity;
import com.madconch.running.base.widget.activity.WebViewActivity;
import com.madconch.running.gallery.PictureManagerActivity;
import com.madconch.running.ui.toast.MadToast;
import com.madconch.running.utillibrary.MadSystemUtil;
import com.madconch.running.utillibrary.savesate.SaveState;

import java.io.File;

import butterknife.OnClick;
import io.reactivex.Observable;
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
//        getTitleBar().instanceTextView(getTitleBar().getRightButtonContainer()).setText("查看");
//        getTitleBar().instanceTextView(getTitleBar().getRightButtonContainer()).setText("取消");
    }

    @Override
    public boolean haveData() {
        return false;
    }

    @OnClick({R.id.btn_01, R.id.btn_02, R.id.btn_03, R.id.btn_04, R.id.btn_05, R.id.btn_06, R.id.btn_07, R.id.btn_08, R.id.btn_09, R.id.btn_10, R.id.btn_11, R.id.btn_12, R.id.btn_13})
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
                break;
            case R.id.btn_11:
                startActivity(WebViewActivity.newIntent(this, "验收标准", "http://zxadmin.dgg.net/orgmain.nk?id=2", false));
                break;
            case R.id.btn_12:
                Observable.just(new TestUpdateEntity())
                        .compose(TransformerProvider.<TestUpdateEntity>provideSchedulers(this))
                        .compose(TransformerProvider.<TestUpdateEntity, IUpdateEntity>convertToSuperInterface())
                        .compose(UpdateManager.provideUpdateTransformer(this, this))
                        .subscribe(new Consumer<UpdateResult>() {
                            @Override
                            public void accept(@NonNull UpdateResult updateResult) throws Exception {
                                switch (updateResult.getResult()) {
                                    case RESULT_NOT_NEED_UPDATE:
                                        MadToast.info("不需要更新");
                                        break;
                                    case RESULT_CANCELED_UPDATE:
                                        MadToast.info("取消更新");
                                        break;
                                    case RESULT_UPDATE_SUCCESS:
                                        MadToast.info("更新成功");
                                        break;
                                    case RESULT_UPDATE_FAILED:
                                        MadToast.info("更新失败");
                                        break;
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                MadToast.error("更新失败");
                            }
                        });

                break;
            case R.id.btn_13: {
                Intent intent = SingeFragmentActivity.newIntent(this, TestSingleFragment.class,TestSingleFragment.newParams("1111"));
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    private class TestUpdateEntity implements IUpdateEntity {

        @Override
        public int getVersionCode() {
            return 100;
        }

        @Override
        public String getVersionName() {
            return "2.3.5";
        }

        @Override
        public String getUpdateContent() {
            return "更新内容是：\n" +
                    "1.修复xxx" +
                    "2.修复223xdad" +
                    "3.修复xsdasfa";
        }

        @Override
        public boolean isForceUpdate() {
            return true;
        }

        @Override
        public String getApkDownloadUrl() {
            return "http://163.177.76.92/mmgr.myapp.com/msoft/sec/secure/GodDresser/1/2/3/102027/tencentmobilemanager_20170510223353_7.0.0_android_build3992_102027.apk?mkey=5928ead3d4cf3655&f=105&c=0&p=.apk";
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
