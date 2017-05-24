package com.madconch.running.base.helper.progress;

import io.reactivex.Flowable;

/**
 * 进度监听器
 */
public class ProgressListener {
    private String id;
    private Flowable<ProgressEntity> listener;

    public String getId() {
        return id;
    }

    public ProgressListener setId(String id) {
        this.id = id;
        return this;
    }

    public Flowable<ProgressEntity> getListener() {
        return listener;
    }

    public ProgressListener setListener(Flowable<ProgressEntity> listener) {
        this.listener = listener;
        return this;
    }
}