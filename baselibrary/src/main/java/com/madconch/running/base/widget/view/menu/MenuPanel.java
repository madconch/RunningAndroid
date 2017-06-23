package com.madconch.running.base.widget.view.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.madconch.running.base.R;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/6/21.
 * Email:496349136@qq.com
 */

public class MenuPanel extends ViewGroup {
    private Drawable divider;
    private int dividerLeftPadding;
    private int dividerRightPadding;

    public MenuPanel(Context context) {
        this(context,null);
    }

    public MenuPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.MenuPanelStyle);
    }

    public MenuPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MenuPanel, defStyleAttr, R.style.DefaultMenuPanelStyle);
        dividerLeftPadding = typedArray.getDimensionPixelSize(R.styleable.MenuPanel_dividerLeftPadding, 0);
        dividerRightPadding = typedArray.getDimensionPixelSize(R.styleable.MenuPanel_dividerRightPadding, 0);
        divider = typedArray.getDrawable(R.styleable.MenuPanel_dividerDrawable);
        typedArray.recycle();
        init();
    }

    private void init() {
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int heightUsed = getPaddingTop();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getLayoutParams() instanceof MarginLayoutParams) {
                MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
                heightUsed += mlp.topMargin;
            }
            int cl = 0;
            int ct = heightUsed;
            int cr = cl + child.getMeasuredWidth();
            int cb = ct + child.getMeasuredHeight();
            child.layout(cl, ct, cr, cb);
            heightUsed += child.getMeasuredHeight();
            if (child.getLayoutParams() instanceof MarginLayoutParams) {
                MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
                heightUsed += mlp.bottomMargin;
            }

            if (divider != null && i != getChildCount() - 1) {
                heightUsed += divider.getIntrinsicHeight();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightUsed = getPaddingTop();
        int maxWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getLayoutParams() instanceof MarginLayoutParams) {
                measureChildWithMargins(child, widthMeasureSpec, getPaddingLeft(), heightMeasureSpec, heightUsed);
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
            heightUsed += child.getMeasuredHeight();

            int useWidth = child.getMeasuredWidth();
            if (child.getLayoutParams() instanceof MarginLayoutParams) {
                MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
                heightUsed += mlp.bottomMargin + mlp.topMargin;
                useWidth += mlp.leftMargin + mlp.rightMargin;
            }

            if (divider != null && i != getChildCount() - 1) {
                heightUsed += divider.getIntrinsicHeight();
            }

            maxWidth = Math.max(maxWidth, useWidth);
        }

        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMeasureMode == MeasureSpec.UNSPECIFIED) {
            widthMeasureSize = maxWidth;
        }

        if (heightMeasureMode != MeasureSpec.EXACTLY) {
            heightMeasureSize = heightUsed;
        }

        setMeasuredDimension(widthMeasureSize, heightMeasureSize);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (divider != null && i != getChildCount() - 1) {
                int left = getPaddingLeft() + dividerLeftPadding;
                int top = child.getBottom();
                if (child.getLayoutParams() instanceof MarginLayoutParams) {
                    MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
                    top += mlp.bottomMargin;
                }
                int right = canvas.getWidth() - getPaddingRight() - dividerRightPadding;
                int bottom = top + 1;
                Rect rect = new Rect(left, top, right, bottom);
                divider.setBounds(rect);
                divider.draw(canvas);
            }
        }
    }
}
