package com.madconch.running.base.widget.activity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/27.
 * Email:496349136@qq.com
 */

public abstract class BaseAbsSplashActivity extends BaseActivity {
    private List<Observable<Boolean>> taskes = new ArrayList<>();
    private int completedTaskCount = 0;
    private boolean isStarted = false;
    private boolean isInitSuccess = true;

    @Override
    protected void onReady() {
        super.onReady();
        startTasks();
    }

    private void startTasks() {
        isStarted = true;
        Observable.merge(taskes)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if(!aBoolean){
                            isInitSuccess = false;
                        }
                        if (++completedTaskCount == taskes.size()) {
                            System.out.println(">>> all task completed");
                            onTaskComplete(isInitSuccess);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        System.out.println(">>> some task errored");
                        throwable.printStackTrace();
                        onTaskComplete(false);
                    }
                });
    }

    /***
     * @param isInitSuccess 是否允许跳转至下级页面
     */
    protected abstract void onTaskComplete(boolean isInitSuccess);

    public void addTask(Observable<Boolean> task) {
        if (isStarted) {
            throw new RuntimeException("con not add task when task started");
        }
        this.taskes.add(task);
    }
}
