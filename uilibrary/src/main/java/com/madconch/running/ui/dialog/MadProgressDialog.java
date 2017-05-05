package com.madconch.running.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.madconch.running.uiconfig.dialog.IProgressDialogConfig;
import com.madconch.running.uiimpl.dialog.ProgressDialogConfig;

/**
 * 功能描述:等待响应进度对话框
 * Created by LuoHaifeng on 2017/5/2.
 * Email:496349136@qq.com
 */

public class MadProgressDialog extends Dialog {
    private TextView messageView;

    public MadProgressDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public MadProgressDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    protected MadProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    protected void init() {
        IProgressDialogConfig config = new ProgressDialogConfig();
        setContentView(config.provideLayoutResource());
        messageView = (TextView) findViewById(config.provideMessageTextViewId());
    }

    public MadProgressDialog setMessage(@StringRes int messageResource) {
        return setMessage(getContext().getResources().getString(messageResource));
    }

    public MadProgressDialog setMessage(String message) {
        messageView.setText(message);
        if (TextUtils.isEmpty(message)) {
            messageView.setVisibility(View.GONE);
        } else {
            messageView.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public TextView getMessageView() {
        return messageView;
    }

    @Override
    public void show() {
        if (TextUtils.isEmpty(messageView.getText().toString())) {
            messageView.setVisibility(View.GONE);
        } else {
            messageView.setVisibility(View.VISIBLE);
        }
        super.show();
    }
}
