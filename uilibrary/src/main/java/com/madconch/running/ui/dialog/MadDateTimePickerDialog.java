package com.madconch.running.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/2.
 * Email:496349136@qq.com
 */

public class MadDateTimePickerDialog extends Dialog {
    public enum ITEM{
        ITEM_YEAR,
        ITEM_MONTH,
    }

    public MadDateTimePickerDialog(@NonNull Context context) {
        super(context);
    }

    public MadDateTimePickerDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected MadDateTimePickerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
