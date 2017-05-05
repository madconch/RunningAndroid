package com.madconch.running.base.common;

import rx.Subscriber;

/**
 * 功能描述:默认空实现了Subscriber,我们只需要处理自己关心的内容
 * Created by LuoHaifeng on 2017/4/13.
 * Email:496349136@qq.com
 */

public class SimpleSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(T t) {

    }
}
