package com.madconch.running.base.widget.activity;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Action;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/27.
 * Email:496349136@qq.com
 */

public abstract class BaseSplashActivity1 extends BaseActivity {
    private int taskCount = 0;
    private boolean taskIsCompleted;

    protected <T> ObservableTransformer<T, T> provideTaskTransformer() {
        taskCount ++;
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                taskCount--;
                                taskIsCompleted = taskCount == 0;
                                if(taskIsCompleted){
                                    onTaskCompleted();
                                }
                            }
                        });
            }
        };
    }

    public boolean taskIsCompeleted() {
        return taskIsCompleted;
    }
    protected abstract void onTaskCompleted();
}
