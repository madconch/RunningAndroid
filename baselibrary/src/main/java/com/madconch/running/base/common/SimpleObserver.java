package com.madconch.running.base.common;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 功能描述:默认空实现了Subscriber,我们只需要处理自己关心的内容
 * Created by LuoHaifeng on 2017/4/13.
 * Email:496349136@qq.com
 */

public class SimpleObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
