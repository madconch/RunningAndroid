package com.madconch.running.utillibrary.proxy;

import java.io.Serializable;

/**
 * 功能描述:startActivityForResult的请求项
 * Created by LuoHaifeng on 2017/5/23.
 * Email:496349136@qq.com
 */

public class RequestBean implements Serializable {
    private int requestCode;
    private int responseCode;
    private ProxyRequestCallback callback;

    public int getRequestCode() {
        return requestCode;
    }

    public RequestBean setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public RequestBean setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public ProxyRequestCallback getCallback() {
        return callback;
    }

    public RequestBean setCallback(ProxyRequestCallback callback) {
        this.callback = callback;
        return this;
    }
}
