package com.madconch.running.base.widget.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * LinearLayoutManager的ItemDecoration
 * Created by LuoHaifeng on 2017/3/3.
 */

public class VerticalItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable dividerDrawable = new ColorDrawable(Color.TRANSPARENT);
    private boolean showHeaderDivider = false;
    private boolean showFooterDivider = false;

    public boolean isShowHeaderDivider() {
        return showHeaderDivider;
    }

    public VerticalItemDecoration setShowHeaderDivider(boolean showHeaderDivider) {
        this.showHeaderDivider = showHeaderDivider;
        return this;
    }

    public boolean isShowFooterDivider() {
        return showFooterDivider;
    }

    public VerticalItemDecoration setShowFooterDivider(boolean showFooterDivider) {
        this.showFooterDivider = showFooterDivider;
        return this;
    }

    public Drawable getDividerDrawable() {
        return dividerDrawable;
    }

    public VerticalItemDecoration setDividerDrawable(Drawable dividerDrawable) {
        this.dividerDrawable = dividerDrawable;
        return this;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int totalItem = parent.getLayoutManager().getItemCount();

        int top = getDrawableHeight(getTopDividerDrawable(position,totalItem));
        int left = getDrawableWidth(getLeftDividerDrawable(position,totalItem));
        int right = getDrawableWidth(getRightDividerDrawable(position,totalItem));
        int bottom = getDrawableHeight(getBottomDividerDrawable(position,totalItem));

        outRect.set(left,top,right,bottom);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        int visibleChildCount = parent.getChildCount();
        int totalItem = parent.getLayoutManager().getItemCount();
        for(int i = 0; i < visibleChildCount; i ++){
            View child = parent.getChildAt(i);
            int left = child.getLeft();
            int right = child.getRight();
            int top = child.getTop();
            int bottom = child.getBottom();

            int position = parent.getChildAdapterPosition(child);

            //draw left
            Drawable leftDividerDrawable = getLeftDividerDrawable(position,totalItem);
            if(leftDividerDrawable != null){
                leftDividerDrawable.setBounds(left - getDrawableWidth(leftDividerDrawable),top,left,bottom);
                leftDividerDrawable.draw(canvas);
            }

            //draw right
            Drawable rightDividerDrawable = getRightDividerDrawable(position,totalItem);
            if(rightDividerDrawable != null){
                rightDividerDrawable.setBounds(right,top,right + getDrawableWidth(rightDividerDrawable),bottom);
                rightDividerDrawable.draw(canvas);
            }

            //draw top
            Drawable topDividerDrawable = getTopDividerDrawable(position,totalItem);
            if(topDividerDrawable != null){
                topDividerDrawable.setBounds(left,top - getDrawableHeight(topDividerDrawable),right,top);
                topDividerDrawable.draw(canvas);
            }

            //draw bottom
            Drawable bottomDividerDrawable = getBottomDividerDrawable(position,totalItem);
            if(bottomDividerDrawable != null){
                bottomDividerDrawable.setBounds(left,bottom,right,bottom + getDrawableHeight(bottomDividerDrawable));
                bottomDividerDrawable.draw(canvas);
            }
        }
    }

    private int getDrawableHeight(Drawable drawable){
        return drawable == null ? 0 : drawable.getIntrinsicHeight();
    }

    private int getDrawableWidth(Drawable drawable){
        return drawable == null ? 0 : drawable.getIntrinsicWidth();
    }

    public Drawable getTopDividerDrawable(int position,int totalItemCount){
        if(position == 0 && isShowHeaderDivider()) {//第一个 检测是否显示顶部分割线
            return dividerDrawable;
        }else{
            return null;
        }
    }

    public Drawable getBottomDividerDrawable(int position,int totalItemCount){
        if(position == totalItemCount - 1 && !isShowFooterDivider()){//最后一行 检测是否需要显示底部分割线
            return null;
        }
        return dividerDrawable;
    }

    public Drawable getLeftDividerDrawable(int position,int totalItemCount){
        return null;
    }

    public Drawable getRightDividerDrawable(int position,int totalItemCount){
        return null;
    }
}
