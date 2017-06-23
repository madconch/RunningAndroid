package com.madconch.running.utillibrary.proxy;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 功能描述:代理的Fragment,支持回调替代startActivityForResult,支持requestCode自动管理
 * Created by LuoHaifeng on 2017/5/23.
 * Email:496349136@qq.com
 */

public class ProxyFragment extends Fragment {
    private SparseArray<ProxyRequestBean> requestCallbacks = new SparseArray<>();
    private final Lock requestCodeLock = new ReentrantLock();
    private int autoRequestCode = 0;

    private synchronized int getAutoRequestCode() {
        synchronized (requestCodeLock) {
            return ++autoRequestCode;
        }
    }

    public Observable<ProxyResultBean> startActivityForResultProxy(@NonNull Intent intent) {
        return startActivityForResultProxy(Activity.RESULT_OK, intent);
    }

    public Observable<ProxyResultBean> startActivityForResultProxy(int resultCode, @NonNull Intent intent) {
        return startActivityForResultProxy(getAutoRequestCode(), resultCode, intent);
    }

    private Observable<ProxyResultBean> startActivityForResultProxy(final int requestCode, final int resultCode, @NonNull final Intent intent) {
        return Observable.create(new ObservableOnSubscribe<ProxyResultBean>() {

            @Override
            public void subscribe(ObservableEmitter<ProxyResultBean> e) throws Exception {
                ProxyRequestBean prb = new ProxyRequestBean().setRequestCode(requestCode).setEmitter(e);
                requestCallbacks.put(requestCode, prb);
                startActivityForResult(intent, requestCode);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ProxyRequestBean requestBean = requestCallbacks.get(requestCode, null);
        if (requestBean != null) {
            requestBean.getEmitter().onNext(new ProxyResultBean().setResultCode(resultCode).setData(data));
            requestBean.getEmitter().onComplete();
            requestCallbacks.remove(requestCode);
        }
    }
}
