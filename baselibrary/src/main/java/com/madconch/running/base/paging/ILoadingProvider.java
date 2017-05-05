package com.madconch.running.base.paging;

import com.madconch.running.ui.loading.ILoadingHelper;

/**
 * 功能描述:Loading
 * Created by LuoHaifeng on 2017/4/13.
 * Email:496349136@qq.com
 */

public interface ILoadingProvider {
    /**
     * @return 当前页面是否已有数据
     */
    boolean haveData();

    /***
     * @return 提供加载工具, 方便布局切换
     */
    ILoadingHelper provideLoadingHelper();
}
