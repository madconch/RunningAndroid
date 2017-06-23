package com.madconch.running.base.widget.view.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.madconch.running.base.R;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/6/21.
 * Email:496349136@qq.com
 */

public class MenuItem extends LinearLayout {
    private AppCompatTextView leftContent;
    private AppCompatTextView rightContent;

    private String leftText = null;
    private String rightText = null;
    private int leftTextColor = Color.parseColor("#333333");
    private int rightTextColor = Color.parseColor("#666666");
    private int leftTextSize = 15;
    private int rightTextSize = 13;
    private Drawable leftLeftDrawable = null;
    private Drawable leftRightDrawable = null;
    private Drawable rightLeftDrawable = null;
    private Drawable rightRightDrawable = null;
    private int leftDrawablePadding = 0;
    private int rightDrawablePadding = 0;

    public MenuItem(Context context) {
        this(context, null);
    }

    public MenuItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.MenuItemStyle);
    }

    public MenuItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MenuItem, defStyleAttr, R.style.DefaultMenuItemStyle);
        leftText = typedArray.getString(R.styleable.MenuItem_leftText);
        rightText = typedArray.getString(R.styleable.MenuItem_rightText);
        leftTextColor = typedArray.getColor(R.styleable.MenuItem_leftTextColor, leftTextColor);
        rightTextColor = typedArray.getColor(R.styleable.MenuItem_rightTextColor, rightTextColor);
        leftTextSize = typedArray.getDimensionPixelSize(R.styleable.MenuItem_leftTextSize, leftTextSize);
        rightTextSize = typedArray.getDimensionPixelSize(R.styleable.MenuItem_rightTextSize, rightTextSize);
        leftLeftDrawable = typedArray.getDrawable(R.styleable.MenuItem_leftLeftDrawable);
        leftRightDrawable = typedArray.getDrawable(R.styleable.MenuItem_leftRightDrawable);
        rightLeftDrawable = typedArray.getDrawable(R.styleable.MenuItem_rightLeftDrawable);
        rightRightDrawable = typedArray.getDrawable(R.styleable.MenuItem_rightRightDrawable);
        leftDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.MenuItem_leftDrawablePadding, leftDrawablePadding);
        rightDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.MenuItem_rightDrawablePadding, rightDrawablePadding);
        typedArray.recycle();
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        leftContent = new AppCompatTextView(getContext());
        leftContent.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        rightContent = new AppCompatTextView(getContext());
        rightContent.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        LayoutParams leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        LayoutParams rightParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        rightParams.weight = 1;

        this.addView(leftContent, leftParams);
        this.addView(rightContent, rightParams);

        leftContent.setCompoundDrawablesWithIntrinsicBounds(leftLeftDrawable, null, leftRightDrawable, null);
        leftContent.setText(leftText);
        leftContent.setTextColor(leftTextColor);
        leftContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
        leftContent.setCompoundDrawablePadding(leftDrawablePadding);

        rightContent.setCompoundDrawablesWithIntrinsicBounds(rightLeftDrawable, null, rightRightDrawable, null);
        rightContent.setText(rightText);
        rightContent.setTextColor(rightTextColor);
        rightContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
        rightContent.setCompoundDrawablePadding(rightDrawablePadding);
    }

    public AppCompatTextView getLeftContent() {
        return leftContent;
    }

    public AppCompatTextView getRightContent() {
        return rightContent;
    }
}
