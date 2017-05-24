package com.madconch.running.base.helper.progress;

import android.support.annotation.NonNull;

import com.madconch.running.base.helper.paging.ILifeCycleProvider;
import com.madconch.running.utillibrary.MadStringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/24.
 * Email:496349136@qq.com
 */

public class ProgressListenerPool {
    private HashMap<String, FlowableEmitter<ProgressEntity>> progressListenerPool = new HashMap<>();
    private List<String> ids = new ArrayList<>();
    private final Lock regLock = new ReentrantLock();
    private static ProgressListenerPool instance = new ProgressListenerPool();

    private ProgressListenerPool() {
    }

    public static ProgressListenerPool getInstance() {
        return instance;
    }

    private String requestRandomId() {
        String id = System.currentTimeMillis() + "" + MadStringUtil.getRandomString(10);
        if (ids.contains(id)) {
            return requestRandomId();
        }
        return id;
    }

    public ProgressListener registListener(ILifeCycleProvider lifeCycleProvider) {
        synchronized (regLock) {
            final String id = requestRandomId();
            ids.add(id);//预定该id
            Flowable<ProgressEntity> listener = Flowable.create(new FlowableOnSubscribe<ProgressEntity>() {
                @Override
                public void subscribe(FlowableEmitter<ProgressEntity> e) throws Exception {
                    progressListenerPool.put(id, e);
                }
            }, BackpressureStrategy.LATEST);
            listener = listener.compose(lifeCycleProvider.<ProgressEntity>bindLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterTerminate(new Action() {
                        @Override
                        public void run() throws Exception {
                            unregistListener(id);
                        }
                    });
            return new ProgressListener().setId(id).setListener(listener);
        }
    }

    public void unregistListener(@NonNull String id) {
        synchronized (regLock) {
            if (ids.contains(id)) {
                ids.remove(id);
            }
            if (progressListenerPool.containsKey(id)) {
                progressListenerPool.remove(id);
            }
        }
    }

    public FlowableEmitter<ProgressEntity> getListener(@NonNull String id) {
        if (progressListenerPool.containsKey(id)) {
            return progressListenerPool.get(id);
        }
        return null;
    }

    public int getSize() {
        return progressListenerPool.size();
    }
}
