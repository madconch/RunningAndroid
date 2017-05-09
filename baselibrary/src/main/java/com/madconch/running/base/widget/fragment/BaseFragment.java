package com.madconch.running.base.widget.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.madconch.running.base.R;
import com.madconch.running.base.helper.paging.ILifeCycleProvider;
import com.madconch.running.base.helper.paging.ILoadingProvider;
import com.madconch.running.ui.loading.ILoadingHelper;
import com.madconch.running.ui.loading.MadLoadingHelper;
import com.madconch.running.ui.toolbar.MadTitleBar;
import com.madconch.running.utillibrary.savesate.MadSaveStateUtil;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/8.
 * Email:496349136@qq.com
 */

public abstract class BaseFragment extends RxFragment implements ILifeCycleProvider, ILoadingProvider {
    private MadTitleBar titleBar;
    private FrameLayout contentContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isOpenAutoSaveState()) {
            try {
                MadSaveStateUtil.injectValue(this, savedInstanceState, getArguments());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * final 请使用init方法进行初始化操作
     *
     * @param savedInstanceState
     */
    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_base, container, false);
        titleBar = (MadTitleBar) view.findViewById(R.id.tb_title_bar);
        if (getActivity() instanceof AppCompatActivity)
            ((AppCompatActivity) getActivity()).setSupportActionBar(titleBar);
        titleBar.setVisibility(View.GONE);

        contentContainer = (FrameLayout) view.findViewById(R.id.fl_container);
        View root = provideContentView(contentContainer);
        if (root != null) {
            contentContainer.addView(root);
        }

        ButterKnife.bind(this, contentContainer);
        internalInit(view, savedInstanceState);
        init(view, savedInstanceState);
        onReady();
        return view;
    }

    protected void internalInit(View root, @Nullable Bundle savedInstanceState) {
    }

    protected void onReady() {
    }

    protected abstract void init(View root, @Nullable Bundle savedInstanceState);

    protected abstract View provideContentView(ViewGroup contentContainer);

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isOpenAutoSaveState()) {
            try {
                MadSaveStateUtil.saveValues(this, outState);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public MadTitleBar getTitleBar() {
        return titleBar;
    }

    public FrameLayout getContentContainer() {
        return contentContainer;
    }

    protected boolean isOpenAutoSaveState() {
        return true;
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

