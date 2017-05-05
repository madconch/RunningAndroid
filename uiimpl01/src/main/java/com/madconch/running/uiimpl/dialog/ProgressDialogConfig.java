package com.madconch.running.uiimpl.dialog;


import com.madconch.running.uiconfig.dialog.IProgressDialogConfig;
import com.madconch.running.uiimpl.R;

/**
 * 功能描述:等待进度对话框样式配置
 * Created by LuoHaifeng on 2017/5/2.
 * Email:496349136@qq.com
 */

public class ProgressDialogConfig implements IProgressDialogConfig {
    @Override
    public int provideLayoutResource() {
        return R.layout.dialog_progress;
    }

    @Override
    public int provideMessageTextViewId() {
        return R.id.tv_message;
    }
}
