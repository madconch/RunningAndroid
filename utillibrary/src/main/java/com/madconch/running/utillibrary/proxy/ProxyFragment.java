package com.madconch.running.utillibrary.proxy;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 功能描述:代理的Fragment,支持回调替代startActivityForResult,支持requestCode自动管理
 * Created by LuoHaifeng on 2017/5/23.
 * Email:496349136@qq.com
 */

public class ProxyFragment extends Fragment {
    private SparseArray<RequestBean> requestCallbacks = new SparseArray<>();
    private final Lock requestCodeLock = new ReentrantLock();
    private int autoRequestCode = 0;

    private synchronized int getAutoRequestCode() {
        synchronized (requestCodeLock) {
            return ++autoRequestCode;
        }
    }

    public void startActivityForResultProxy(@NonNull Intent intent, @NonNull ProxyRequestCallback callback) {
        startActivityForResultProxy(Activity.RESULT_OK, intent, callback);
    }

    public void startActivityForResultProxy(int resultCode, @NonNull Intent intent, @NonNull ProxyRequestCallback callback) {
        startActivityForResultProxy(getAutoRequestCode(), resultCode, intent, callback);
    }

    private void startActivityForResultProxy(int requestCode, int resultCode, @NonNull Intent intent, @NonNull ProxyRequestCallback callback) {
        RequestBean requestBean = new RequestBean().setRequestCode(requestCode).setResponseCode(resultCode).setCallback(callback);
        requestCallbacks.put(requestBean.getRequestCode(), requestBean);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RequestBean requestBean = requestCallbacks.get(requestCode, null);
        if (requestBean != null) {
            if (resultCode == requestBean.getResponseCode()) {
                requestBean.getCallback().onResult(data);
            } else {
                requestBean.getCallback().onOther(data);
            }
            requestCallbacks.remove(requestCode);
        }
    }
}
