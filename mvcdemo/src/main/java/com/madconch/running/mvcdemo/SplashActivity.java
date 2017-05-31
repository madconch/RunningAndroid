package com.madconch.running.mvcdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madconch.running.base.common.TransformerProvider;
import com.madconch.running.base.helper.update.IUpdateEntity;
import com.madconch.running.base.helper.update.UpdateManager;
import com.madconch.running.base.helper.update.UpdateResult;
import com.madconch.running.base.widget.activity.BaseSplashActivity1;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.madconch.running.base.helper.update.UpdateResult.Result.RESULT_UPDATE_FAILED;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/27.
 * Email:496349136@qq.com
 */

public class SplashActivity extends BaseSplashActivity1 {
    private UpdateResult.Result updateResult;
    private boolean isForceUpdate = false;

    @Override
    protected View provideContentView(ViewGroup container) {
        return LayoutInflater.from(getContext()).inflate(R.layout.activity_splash, container, false);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Observable.timer(1500, TimeUnit.MILLISECONDS)
                .compose(TransformerProvider.<Long>provideSchedulers(this))
                .compose(this.<Long>provideTaskTransformer())
                .subscribe();
        Observable.just(new TestUpdateEntity())
                .compose(TransformerProvider.<TestUpdateEntity>provideSchedulers(this))
                .compose(TransformerProvider.<TestUpdateEntity, IUpdateEntity>convertToSuperInterface())
                .compose(this.<IUpdateEntity>provideTaskTransformer())
                .compose(UpdateManager.provideUpdateTransformer(this, this))
                .subscribe(new Consumer<UpdateResult>() {
                    @Override
                    public void accept(@NonNull UpdateResult updateResultEntity) throws Exception {
                        isForceUpdate = updateResultEntity.getUpdateEntity().isForceUpdate();
                        updateResult = updateResultEntity.getResult();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        updateResult = RESULT_UPDATE_FAILED;
                    }
                });
    }

    @Override
    protected void onTaskCompleted() {
        boolean isJump = false;
        switch (updateResult) {
            case RESULT_NOT_NEED_UPDATE:
                isJump = true;
                break;
            case RESULT_CANCELED_UPDATE:
                isJump = !isForceUpdate;
                break;
            case RESULT_UPDATE_SUCCESS:
                isJump = false;
                break;
            case RESULT_UPDATE_FAILED:
                isJump = !isForceUpdate;
                break;
        }

        if(isJump){
            startActivity(new Intent(this,MainActivity.class));
        }

        finish();
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
            return false;
        }

        @Override
        public String getApkDownloadUrl() {
            return "http://163.177.76.92/mmgr.myapp.com/msoft/sec/secure/GodDresser/1/2/3/102027/tencentmobilemanager_20170510223353_7.0.0_android_build3992_102027.apk?mkey=5928ead3d4cf3655&f=105&c=0&p=.apk";
        }
    }
}
