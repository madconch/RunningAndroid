package com.madconch.running.base.helper.paging;

/**
 * 功能描述:分页内容提供接口
 * Created by LuoHaifeng on 2017/4/13.
 * Email:496349136@qq.com
 */

public interface IPagingProvider extends IRefreshProvider {
    /**
     * @return 当前页面是否还有更多的数据（是否需要加载更多）
     */
    boolean haveMoreData();
}
