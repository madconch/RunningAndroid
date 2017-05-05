package com.madconch.running.base.helper.paging;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * 功能描述:提供请求的生命周期管理
 * Created by LuoHaifeng on 2017/4/20.
 * Email:496349136@qq.com
 */

public interface ILifeCycleProvider {
    <T> LifecycleTransformer<T> bindLifecycle();
}
