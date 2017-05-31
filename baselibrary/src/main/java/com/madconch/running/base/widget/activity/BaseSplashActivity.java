package com.madconch.running.base.widget.activity;

import android.os.Bundle;

import com.madconch.running.base.common.TransformerProvider;
import com.madconch.running.base.helper.update.IUpdateEntity;
import com.madconch.running.base.helper.update.UpdateManager;
import com.madconch.running.base.helper.update.UpdateResult;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/26.
 * Email:496349136@qq.com
 */

public abstract class BaseSplashActivity extends BaseAbsSplashActivity {
    private Observable<Boolean> delayTask;
    private Observable<Boolean> updateTask;

    @Override
    protected void internalInit(Bundle savedInstanceState) {
        super.internalInit(savedInstanceState);
        delayTask = Observable.just(true)
                .delay(provideMinDelay(), TimeUnit.MILLISECONDS)
                .compose(TransformerProvider.<Boolean>provideSchedulers(this))
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull Boolean aBoolean) throws Exception {
                        System.out.println(">>> delay task completed");
                        return aBoolean;
                    }
                });
        addTask(delayTask);

        Observable<IUpdateEntity> upTask = provideUpdateTask();
        if (upTask != null) {
            updateTask = provideUpdateTask()
                    .compose(UpdateManager.provideUpdateTransformer(this, this))
                    .map(new Function<UpdateResult, Boolean>() {
                        @Override
                        public Boolean apply(@NonNull UpdateResult updateResult) throws Exception {
                            System.out.println(">>> update task completed");
                            switch (updateResult.getResult()) {
                                case RESULT_NOT_NEED_UPDATE:
                                    return true;
                                case RESULT_CANCELED_UPDATE:
                                case RESULT_UPDATE_SUCCESS:
                                case RESULT_UPDATE_FAILED:
                                    return !updateResult.getUpdateEntity().isForceUpdate();
                            }
                            return null;
                        }
                    });
            addTask(updateTask);
        }
    }

    protected abstract Observable<IUpdateEntity> provideUpdateTask();

    protected long provideMinDelay() {
        return 1500;
    }

    /***
     * @param isInitSuccess 是否允许跳转至下级页面
     */
    protected abstract void onTaskComplete(boolean isInitSuccess);
}
