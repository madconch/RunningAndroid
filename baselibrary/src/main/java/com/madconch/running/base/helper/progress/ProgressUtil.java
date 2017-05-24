package com.madconch.running.base.helper.progress;

import io.reactivex.FlowableEmitter;

/**
 * 功能描述:快速进行进度操作工具
 * Created by LuoHaifeng on 2017/5/24.
 * Email:496349136@qq.com
 */

class ProgressUtil {
    static void onNext(String progressListenerId, ProgressEntity progressEntity) {
        FlowableEmitter<ProgressEntity> emitter = ProgressListenerPool.getInstance().getListener(progressListenerId);
        if (emitter != null && !emitter.isCancelled()) {
            emitter.onNext(progressEntity);
        }
    }

    static void onComplete(String progressListenerId) {
        FlowableEmitter<ProgressEntity> emitter = ProgressListenerPool.getInstance().getListener(progressListenerId);
        if (emitter != null && !emitter.isCancelled()) {
            emitter.onComplete();
        }
    }

    static void onError(String progressListenerId,Throwable throwable){
        FlowableEmitter<ProgressEntity> emitter = ProgressListenerPool.getInstance().getListener(progressListenerId);
        if (emitter != null && !emitter.isCancelled()) {
            emitter.onError(throwable);
        }
    }
}
