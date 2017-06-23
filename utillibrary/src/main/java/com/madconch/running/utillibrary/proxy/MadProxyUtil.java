package com.madconch.running.utillibrary.proxy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import io.reactivex.Observable;

/**
 * 功能描述:通过代理Fragment简化startActivityForResult
 * Created by LuoHaifeng on 2017/5/23.
 * Email:496349136@qq.com
 */

public class MadProxyUtil {
    private final static String PROXY_TAG = "IDENTITY_PROXY_TAG";

    /**
     * 通过Activity获取ProxyFragment实例,寻找Activity中是否包含ProxyFragment实例,如果有那么直接返回,否则在Activity中添加Fragment
     *
     * @param activity 指定的activity
     * @return 代理Fragment
     */
    private static ProxyFragment getProxyFragmentByActivity(@NonNull FragmentActivity activity) {
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(PROXY_TAG);
        ProxyFragment proxyFragment;
        if (fragment != null) {
            proxyFragment = (ProxyFragment) fragment;
        } else {
            proxyFragment = new ProxyFragment();
            activity.getSupportFragmentManager().beginTransaction().add(proxyFragment, PROXY_TAG).commitNowAllowingStateLoss();
        }
        return proxyFragment;
    }

    /**
     * 通过Fragment获取ProxyFragment实例,先判断原有的Fragment是否是ProxyFragment实例,如果是直接返回;
     * 再判断原Fragment的子Fragment中是否有ProxyFragment实例,有则直接返回;
     * 否则,添加子Fragment
     *
     * @param srcFragment 指定的activity
     * @return 代理Fragment
     */
    private static ProxyFragment getProxyFragmentByFragment(@NonNull Fragment srcFragment) {
        ProxyFragment proxyFragment;
        if (srcFragment instanceof ProxyFragment) {
            proxyFragment = (ProxyFragment) srcFragment;
        } else {
            Fragment fragment = srcFragment.getChildFragmentManager().findFragmentByTag(PROXY_TAG);
            if (fragment != null) {
                proxyFragment = (ProxyFragment) fragment;
            } else {
                proxyFragment = new ProxyFragment();
                srcFragment.getChildFragmentManager().beginTransaction().add(proxyFragment, PROXY_TAG).commitNowAllowingStateLoss();
            }
        }

        return proxyFragment;
    }

    /**
     * 代理跳转页面，替代startActivityForResult方法,当resultCode为Activity.RESULT_OK的时候回调成功
     *
     * @param activity 需要发起跳转的页面
     * @param intent   需要跳转的目标
     */
    public static Observable<ProxyResultBean> startActivityForResultProxy(@NonNull FragmentActivity activity, @NonNull Intent intent) {
        return getProxyFragmentByActivity(activity).startActivityForResultProxy(intent);
    }

    /**
     * 代理跳转页面，替代startActivityForResult方法,当resultCode为resultCode的时候回调成功
     *
     * @param activity   需要发起跳转的页面
     * @param intent     需要跳转的目标
     * @param resultCode 指定判定成功的code
     */
    public static Observable<ProxyResultBean> startActivityForResultProxy(@NonNull FragmentActivity activity, int resultCode, @NonNull Intent intent) {
        return getProxyFragmentByActivity(activity).startActivityForResultProxy(resultCode, intent);
    }

    /**
     * 代理跳转页面，替代startActivityForResult方法,当resultCode为Activity.RESULT_OK的时候回调成功
     *
     * @param fragment 需要发起跳转的页面
     * @param intent   需要跳转的目标
     */
    public static Observable<ProxyResultBean> startActivityForResultProxy(@NonNull Fragment fragment, @NonNull Intent intent) {
        return getProxyFragmentByFragment(fragment).startActivityForResultProxy(intent);
    }

    /**
     * 代理跳转页面，替代startActivityForResult方法,当resultCode为resultCode的时候回调成功
     *
     * @param fragment   需要发起跳转的页面
     * @param intent     需要跳转的目标
     * @param resultCode 指定判定成功的code
     */
    public static Observable<ProxyResultBean> startActivityForResultProxy(@NonNull Fragment fragment, int resultCode, @NonNull Intent intent) {
        return getProxyFragmentByFragment(fragment).startActivityForResultProxy(resultCode, intent);
    }
}
