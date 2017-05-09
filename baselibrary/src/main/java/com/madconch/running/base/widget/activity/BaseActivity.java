package com.madconch.running.base.widget.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.madconch.running.base.R;
import com.madconch.running.base.helper.paging.ILifeCycleProvider;
import com.madconch.running.base.helper.paging.ILoadingProvider;
import com.madconch.running.ui.loading.ILoadingHelper;
import com.madconch.running.ui.loading.MadLoadingHelper;
import com.madconch.running.ui.toolbar.MadTitleBar;
import com.madconch.running.utillibrary.savesate.MadSaveStateUtil;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/8.
 * Email:496349136@qq.com
 */

public abstract class BaseActivity extends RxAppCompatActivity implements ILifeCycleProvider, ILoadingProvider {
    private MadTitleBar titleBar;
    private FrameLayout contentContainer;
    private ImageView btnBack;

    /**
     * final 请使用init方法进行初始化操作
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.layout_base);
        internalInit(savedInstanceState);//内部初始化操作(框架内容初始化在此方法中进行,以确保最后一级页面在初始化的时候内部初始化已经完毕)
        init(savedInstanceState);//在本方法中进行初始化操作代替在onCreate中操作(此方法提供给最后一级页面处理)
        onReady();//所有数据准备完毕之后执行,请保证这是onCreate()的最后一步
    }

    protected void init(Bundle savedInstanceState) {

    }

    protected abstract View provideContentView(ViewGroup container);

    protected void internalInit(Bundle savedInstanceState) {
        //参数注入
        if (isOpenAutoSaveState()) {
            try {
                MadSaveStateUtil.injectValue(this, savedInstanceState, getIntent().getExtras());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //标题栏
        titleBar = (MadTitleBar) findViewById(R.id.tb_title_bar);
        setSupportActionBar(titleBar);
        //沉浸式状态栏
        initStatusBar();
        //主体内容
        contentContainer = (FrameLayout) findViewById(R.id.fl_container);
        setContentView(provideContentView(contentContainer));
        //View注入
        ButterKnife.bind(this,contentContainer);
    }

    protected void onReady() {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //参数保存
        if (isOpenAutoSaveState()) {
            try {
                MadSaveStateUtil.saveValues(this, outState);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, contentContainer, true);
    }

    @Override
    public void setContentView(View view) {
        if (view != null) {
            contentContainer.addView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view != null) {
            contentContainer.addView(view, params);
        }
    }

    /***
     * 沉浸式状态栏
     */
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            );
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(provideStatusBarColor());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemStatusManager tintManager = new SystemStatusManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            // 设置状态栏的颜色
            tintManager.setStatusBarTintColor(provideStatusBarColor());
            getWindow().getDecorView().setFitsSystemWindows(true);
        }

        if ((findViewById(android.R.id.content)) != null) {
            View container = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            if (container != null) {
                container.setFitsSystemWindows(isFitsSystemWindows());
            }
        }
    }

    protected int provideStatusBarColor() {
        return getResources().getColor(com.madconch.running.ui.R.color.theme_color);
    }

    protected boolean isFitsSystemWindows() {
        return true;
    }

    protected boolean isOpenAutoSaveState() {
        return true;
    }

    public MadTitleBar getTitleBar() {
        return titleBar;
    }

    public FrameLayout getContentContainer() {
        return contentContainer;
    }

    public Context getContext() {
        return this;
    }

    public synchronized void showBackButton() {
        if (btnBack == null) {
            btnBack = titleBar.showBackButton();
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        btnBack.setVisibility(View.VISIBLE);
    }

    public void hideBackButton() {
        if (btnBack != null) {
            btnBack.setVisibility(View.GONE);
        }
    }

    public ImageView getBtnBack() {
        return btnBack;
    }

    @Override
    public <T> LifecycleTransformer<T> bindLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public ILoadingHelper provideLoadingHelper() {
        return MadLoadingHelper.with(getContentContainer());
    }
}
