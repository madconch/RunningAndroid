package com.madconch.running.ui.toolbar;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.madconch.running.uiconfig.toolbar.ITitlelbarConfig;
import com.madconch.running.uiimpl.toolbar.TitleBarConfig;

/**
 * 标题栏
 * Created by LuoHaifeng on 2017/3/8.
 */

public class MadTitleBar extends Toolbar {
    private ITitlelbarConfig config = new TitleBarConfig();
    public static final int DRAWABLE_LEFT = 1;
    public static final int DRAWABLE_RIGHT = 2;
    public static final int DRAWABLE_TOP = 3;
    public static final int DRAWABLE_BOTTOM = 4;

    private LinearLayout leftButtonContainer, rightButtonContainer;
    private TextView titleView;
    private ImageView btnBack;

    public MadTitleBar(Context context) {
        super(context);
        init();
    }

    public MadTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MadTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        leftButtonContainer = new LinearLayout(getContext());
        LayoutParams leftButtonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        leftButtonParams.gravity = Gravity.START;
        leftButtonContainer.setLayoutParams(leftButtonParams);

        rightButtonContainer = new LinearLayout(getContext());
        LayoutParams rightButtonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        rightButtonParams.gravity = Gravity.END;
        rightButtonContainer.setLayoutParams(rightButtonParams);

        titleView = new TextView(getContext());
        LayoutParams titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        titleParams.gravity = Gravity.CENTER;
        titleView.setLayoutParams(titleParams);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextAppearance(getContext(), config.provideTitleStyle());

        btnBack = getImageButton(config.provideBackButtonImageResource());

        this.addView(leftButtonContainer);
        this.addView(titleView);
        this.addView(rightButtonContainer);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        super.setTitle("");
        titleView.setText(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle("");
        titleView.setText(title);
    }

    public ImageView showBackButton() {
        if (leftButtonContainer.getChildCount() > 0 && leftButtonContainer.getChildAt(0) == btnBack) {
            return btnBack;
        }

        leftButtonContainer.addView(btnBack, 0);
        return btnBack;
    }

    public TextView addLeftImageTextButton(@StringRes int textId, @DrawableRes int resId, int drawablePosition) {
        TextView button = getTextImageButton(textId, resId, drawablePosition);
        leftButtonContainer.addView(button);
        int padding = config.provideMenuSpacing();
        button.setPadding(padding, 0, 0, 0);
        return button;
    }

    public TextView addRightImageTextButton(@StringRes int textId, @DrawableRes int resId, int drawablePosition) {
        TextView button = getTextImageButton(textId, resId, drawablePosition);
        rightButtonContainer.addView(button);
        int padding = config.provideMenuSpacing();
        button.setPadding(0, 0, padding, 0);
        return button;
    }

    public TextView addLeftTextButton(@StringRes int resId) {
        TextView button = getTextButton(resId);
        leftButtonContainer.addView(button);
        int padding = config.provideMenuSpacing();
        button.setPadding(padding, 0, 0, 0);
        return button;
    }

    public TextView addRightTextButton(@StringRes int resId) {
        TextView button = getTextButton(resId);
        rightButtonContainer.addView(button);
        int padding = config.provideMenuSpacing();
        button.setPadding(0, 0, padding, 0);
        return button;
    }

    public ImageView addLeftImageButton(@DrawableRes int resId) {
        ImageView button = getImageButton(resId);
        leftButtonContainer.addView(button);
        int padding = config.provideMenuSpacing();
        button.setPadding(padding, 0, 0, 0);
        return button;
    }

    public ImageView addRightImageButton(@DrawableRes int resId) {
        ImageView button = getImageButton(resId);
        rightButtonContainer.addView(button);
        int padding = config.provideMenuSpacing();
        button.setPadding(0, 0, padding, 0);
        return button;
    }

    private TextView getTextButton(@StringRes int resId) {
        TextView button = new TextView(this.getContext());
        button.setText(resId);
        button.setGravity(Gravity.CENTER);
        button.setTextAppearance(getContext(), config.provideMenuStyle());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        button.setLayoutParams(lp);
        return button;
    }

    private ImageView getImageButton(@DrawableRes int resId) {
        ImageView button = new ImageView(this.getContext());
        button.setImageResource(resId);
        button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        button.setLayoutParams(lp);
        return button;
    }

    private TextView getTextImageButton(@StringRes int textId, @DrawableRes int resId, int drawablePosition) {
        TextView button = getTextButton(textId);
        button.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
        if (drawablePosition == DRAWABLE_LEFT) {
            button.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        } else if (drawablePosition == DRAWABLE_TOP) {
            button.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
        } else if (drawablePosition == DRAWABLE_RIGHT) {
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        } else if (drawablePosition == DRAWABLE_BOTTOM) {
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, resId);
        }

        return button;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public LinearLayout getLeftButtonContainer() {
        return leftButtonContainer;
    }

    public LinearLayout getRightButtonContainer() {
        return rightButtonContainer;
    }

}
