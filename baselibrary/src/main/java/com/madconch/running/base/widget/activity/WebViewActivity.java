package com.madconch.running.base.widget.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.madconch.running.base.R;
import com.madconch.running.utillibrary.MadCacheUtil;
import com.madconch.running.utillibrary.savesate.SaveState;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;

/**
 * 功能描述:提供基于腾讯x5内核的简易浏览器
 * Created by LuoHaifeng on 2017/5/25.
 * Email:496349136@qq.com
 */

public class WebViewActivity extends BaseActivity {
    ProgressBar progressBar;
    WebView webView;
    @SaveState(key = "title")
    String title;
    @SaveState(key = "url")
    String url;
    @SaveState(key = "isSimpleMode")
    boolean isSimpleMode = false;

    AppCompatImageView btnGoBack, btnGoForward, btnStop, btnReload;
    ViewGroup operateBar;

    @Override
    protected View provideContentView(ViewGroup container) {
        return LayoutInflater.from(getContext()).inflate(R.layout.layout_webview, container, false);
    }

    public static Intent newIntent(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("isSimpleMode", false);
        return intent;
    }

    public static Intent newIntent(Context context, String title, String url, boolean isSimpleMode) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("isSimpleMode", isSimpleMode);
        return intent;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle(title);
        showBackButton();

        getWindow().setFormat(PixelFormat.TRANSLUCENT);//避免网页中的视频，上屏幕的时候，可能出现闪烁的情况

        operateBar = (ViewGroup) findViewById(R.id.flex_operate_container);
        progressBar = (ProgressBar) findViewById(R.id.pb_progress_bar);
        webView = (WebView) findViewById(R.id.wv_content);

        initWebView();
        initOperateBar();
        load();
    }

    private void initOperateBar() {
        btnGoBack = (AppCompatImageView) findViewById(R.id.btn_go_back);
        btnGoForward = (AppCompatImageView) findViewById(R.id.btn_go_forward);
        btnStop = (AppCompatImageView) findViewById(R.id.btn_stop_loading);
        btnReload = (AppCompatImageView) findViewById(R.id.btn_reload);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

        btnGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoForward()) {
                    webView.goForward();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.stopLoading();
            }
        });

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });

        ColorStateList tintColorList = getResources().getColorStateList(R.color.color_list_webview_operate_button);
        DrawableCompat.setTintList(btnGoBack.getDrawable(), tintColorList);
        DrawableCompat.setTintList(btnGoForward.getDrawable(), tintColorList);
        DrawableCompat.setTintList(btnStop.getDrawable(), tintColorList);
        DrawableCompat.setTintList(btnReload.getDrawable(), tintColorList);

        btnGoBack.setEnabled(false);
        btnGoForward.setEnabled(false);
        btnStop.setEnabled(false);
        btnReload.setEnabled(false);

        if (isSimpleMode) {
            operateBar.setVisibility(View.GONE);
        } else {
            operateBar.setVisibility(View.VISIBLE);
        }
    }

    private void updateBackAndForwardState() {
        btnGoBack.setEnabled(webView.canGoBack());
        btnGoForward.setEnabled(webView.canGoForward());
    }

    private void load() {
        webView.loadUrl(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    private void initWebView() {
        webView.setWebChromeClient(new WebChromeClient() {
            View videoView;
            View normalView;
            IX5WebChromeClient.CustomViewCallback callback;

            @Override
            public void onShowCustomView(View view,
                                         IX5WebChromeClient.CustomViewCallback customViewCallback) {
                LinearLayout normalView = (LinearLayout) findViewById(R.id.ll_container);
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                this.videoView = view;
                this.normalView = normalView;
                this.callback = customViewCallback;
            }

            @Override
            public void onHideCustomView() {
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }

                if (videoView != null) {
                    ViewGroup viewGroup = (ViewGroup) videoView.getParent();
                    viewGroup.removeView(videoView);
                    viewGroup.addView(normalView);
                }
            }

            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                progressBar.setMax(100);
                progressBar.setProgress(i);
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                progressBar.setVisibility(View.VISIBLE);
                updateBackAndForwardState();
                btnStop.setEnabled(true);
                btnReload.setEnabled(false);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                progressBar.setVisibility(View.GONE);
                updateBackAndForwardState();
                btnStop.setEnabled(false);
                btnReload.setEnabled(true);
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {

            }
        });

        WebSettings webSetting = webView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(new File(MadCacheUtil.getCacheDir(), "webkit-cache").getAbsolutePath());
        webSetting.setDatabasePath(new File(MadCacheUtil.getCacheDir(), "databases").getAbsolutePath());
        webSetting.setGeolocationDatabasePath(new File(MadCacheUtil.getCacheDir(), "locations").getAbsolutePath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

    public WebView getWebView() {
        return webView;
    }

    public ViewGroup getOperateBar() {
        return operateBar;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !isSimpleMode) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webView != null){
            webView.destroy();
            webView = null;
        }
    }
}
