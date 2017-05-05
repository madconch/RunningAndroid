package com.madconch.running.base.helper.paging;

import com.madconch.running.uiconfig.refresh.IRefreshLayout;

/**
 * 功能描述:上下拉内容提供接口
 * Created by LuoHaifeng on 2017/4/21.
 * Email:496349136@qq.com
 */

public interface IRefreshProvider extends ILoadingProvider {
    IRefreshLayout getRefreshLayout();
}
