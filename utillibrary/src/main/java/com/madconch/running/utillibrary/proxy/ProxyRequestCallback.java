package com.madconch.running.utillibrary.proxy;

import android.content.Intent;

/**
 * 功能描述:使用代理startActivityForResult的回调接口
 * Created by LuoHaifeng on 2017/5/23.
 * Email:496349136@qq.com
 */

public interface ProxyRequestCallback {
    void onResult(Intent data);
    void onOther(Intent data);
}
