package com.madconch.running.uiimpl.refresh;

import android.content.Context;
import android.util.AttributeSet;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

/**
 * 功能描述:定制TwinkRefreshLayout
 * Created by LuoHaifeng on 2017/5/3.
 * Email:496349136@qq.com
 */

public class FixTwinkRefreshLayout extends TwinklingRefreshLayout {

    public FixTwinkRefreshLayout(Context context) {
        this(context, null);
    }

    public FixTwinkRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FixTwinkRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //修复使用new FixTwinkRefreshLayout(Context context) 没有初始化onFinishInflate里面的内容从而导致Crash
        post(new Runnable() {
            @Override
            public void run() {
                onFinishInflate();
            }
        });
    }

    public void proxyOnFinishInflate(){
        onFinishInflate();
    }
}
