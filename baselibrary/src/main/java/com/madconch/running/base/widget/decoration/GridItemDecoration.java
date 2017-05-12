package com.madconch.running.base.widget.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * GridLayoutManager的ItemDecoration
 * Created by LuoHaifeng on 2017/3/7.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable verticalDividerDrawable;
    private Drawable horizontalDividerDrawable;
    private boolean showTopDivider = false;
    private boolean showLeftDivider = false;
    private boolean showRightDivider = false;
    private boolean showBottomDivider = false;

    public Drawable getVerticalDividerDrawable() {
        return verticalDividerDrawable;
    }

    public GridItemDecoration setVerticalDividerDrawable(Drawable verticalDividerDrawable) {
        this.verticalDividerDrawable = verticalDividerDrawable;
        return this;
    }

    public Drawable getHorizontalDividerDrawable() {
        return horizontalDividerDrawable;
    }

    public GridItemDecoration setHorizontalDividerDrawable(Drawable horizontalDividerDrawable) {
        this.horizontalDividerDrawable = horizontalDividerDrawable;
        return this;
    }

    public boolean isShowTopDivider() {
        return showTopDivider;
    }

    public GridItemDecoration setShowTopDivider(boolean showTopDivider) {
        this.showTopDivider = showTopDivider;
        return this;
    }

    public boolean isShowLeftDivider() {
        return showLeftDivider;
    }

    public GridItemDecoration setShowLeftDivider(boolean showLeftDivider) {
        this.showLeftDivider = showLeftDivider;
        return this;
    }

    public boolean isShowRightDivider() {
        return showRightDivider;
    }

    public GridItemDecoration setShowRightDivider(boolean showRightDivider) {
        this.showRightDivider = showRightDivider;
        return this;
    }

    public boolean isShowBottomDivider() {
        return showBottomDivider;
    }

    public GridItemDecoration setShowBottomDivider(boolean showBottomDivider) {
        this.showBottomDivider = showBottomDivider;
        return this;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int colCount = layoutManager.getSpanCount(); //列数
        int totalCount = layoutManager.getItemCount();
        int maxRowIndex = totalCount % colCount == 0 ? (totalCount / colCount) : (totalCount / colCount + 1);
        int maxColIndex = colCount - 1;

        int curChildCount = parent.getChildCount();
        for(int i = 0; i < curChildCount; i ++){
            View child = parent.getChildAt(i);
            int left = child.getLeft();
            int right = child.getRight();
            int top = child.getTop();
            int bottom = child.getBottom();

            int position = parent.getChildAdapterPosition(child);
            int curColIndex = position % colCount;
            int curRowIndex = position / colCount;

            Drawable leftDividerDrawable = getLeftDrawable(position,totalCount,maxColIndex,maxRowIndex,curColIndex,curRowIndex);
            Drawable topDividerDrawable = getTopDrawable(position,totalCount,maxColIndex,maxRowIndex,curColIndex,curRowIndex);
            Drawable rightDividerDrawable = getRightDrawable(position,totalCount,maxColIndex,maxRowIndex,curColIndex,curRowIndex);
            Drawable bottomDividerDrawable = getBottomDrawable(position,totalCount,maxColIndex,maxRowIndex,curColIndex,curRowIndex);

            //draw left
            if(leftDividerDrawable != null){
                leftDividerDrawable.setBounds(left - getDrawableWidth(leftDividerDrawable),top,left,bottom);
                leftDividerDrawable.draw(canvas);
            }

            //draw right
            if(rightDividerDrawable != null){
                rightDividerDrawable.setBounds(right,top,right + getDrawableWidth(rightDividerDrawable),bottom);
                rightDividerDrawable.draw(canvas);
            }

            //draw top
            if(topDividerDrawable != null){
                topDividerDrawable.setBounds(left,top - getDrawableHeight(topDividerDrawable),right,top);
                topDividerDrawable.draw(canvas);
            }

            //draw bottom
            if(bottomDividerDrawable != null){
                bottomDividerDrawable.setBounds(left,bottom,right,bottom + getDrawableHeight(bottomDividerDrawable));
                bottomDividerDrawable.draw(canvas);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int colCount = layoutManager.getSpanCount(); //列数
        int position = parent.getChildAdapterPosition(view);
        int totalCount = layoutManager.getItemCount();
        int maxRowIndex = totalCount % colCount == 0 ? (totalCount / colCount) : (totalCount / colCount + 1);
        int maxColIndex = colCount - 1;
        int curColIndex = position % colCount;
        int curRowIndex = position / colCount;

        int left = getDrawableWidth(getLeftDrawable(position,totalCount,maxColIndex,maxRowIndex,curColIndex,curRowIndex));
        int top = getDrawableHeight(getTopDrawable(position,totalCount,maxColIndex,maxRowIndex,curColIndex,curRowIndex));
        int right = getDrawableWidth(getRightDrawable(position,totalCount,maxColIndex,maxRowIndex,curColIndex,curRowIndex));
        int bottom = getDrawableHeight(getBottomDrawable(position,totalCount,maxColIndex,maxRowIndex,curColIndex,curRowIndex));

        outRect.set(left,top,right,bottom);

    }

    private int getDrawableHeight(Drawable drawable){
        return drawable == null ? 0 : drawable.getIntrinsicHeight();
    }

    private int getDrawableWidth(Drawable drawable){
        return drawable == null ? 0 : drawable.getIntrinsicWidth();
    }

    private Drawable getLeftDrawable(int position,int totalCount,int maxColIndex,int maxRowIndex,int curColIndex,int curRowIndex){
        if(curColIndex == 0 && showLeftDivider){
            return verticalDividerDrawable;
        }
        return null;
    }
    private Drawable getRightDrawable(int position,int totalCount,int maxColIndex,int maxRowIndex,int curColIndex,int curRowIndex){
        if(curColIndex == maxColIndex  && !showRightDivider){
            return null;
        }
        return verticalDividerDrawable;
    }
    private Drawable getTopDrawable(int position,int totalCount,int maxColIndex,int maxRowIndex,int curColIndex,int curRowIndex){
        if(curRowIndex == 0 && showTopDivider){
            return horizontalDividerDrawable;
        }
        return null;
    }
    private Drawable getBottomDrawable(int position,int totalCount,int maxColIndex,int maxRowIndex,int curColIndex,int curRowIndex){
        if(curRowIndex == maxRowIndex && !showBottomDivider){
            return null;
        }
        return horizontalDividerDrawable;
    }
}
