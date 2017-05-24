package com.madconch.running.base.helper.progress;

/**
 * 功能描述:请求进度实体
 * Created by LuoHaifeng on 2017/5/24.
 * Email:496349136@qq.com
 */

public class ProgressEntity {
    private long total;//总长度
    private long progress;//进度
    private boolean isCompleted;//该阶段是否已完成（请求、响应两种阶段）
    private boolean isRequest;//是否是请求阶段,true表示属于请求阶段,false表示属于响应阶段

    public long getTotal() {
        return total;
    }

    public ProgressEntity setTotal(long total) {
        this.total = total;
        return this;
    }

    public long getProgress() {
        return progress;
    }

    public ProgressEntity setProgress(long progress) {
        this.progress = progress;
        return this;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public ProgressEntity setCompleted(boolean completed) {
        isCompleted = completed;
        return this;
    }

    public boolean isRequest() {
        return isRequest;
    }

    public ProgressEntity setRequest(boolean request) {
        isRequest = request;
        return this;
    }

    @Override
    public String toString() {
        return progress + "/" + total + " isCompeletd:" + isCompleted() + "  isRequest:" + isRequest();
    }
}
