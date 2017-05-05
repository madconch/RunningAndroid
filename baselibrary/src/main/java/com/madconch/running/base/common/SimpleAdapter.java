package com.madconch.running.base.common;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * 功能描述:简易的Adapter
 * Created by LuoHaifeng on 2017/4/20.
 * Email:496349136@qq.com
 */


public abstract class SimpleAdapter<T,V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    private ArrayList<T>  datas = new ArrayList<>();

    public ArrayList<T> getDatas() {
        return datas;
    }

    public SimpleAdapter setDatas(ArrayList<T> datas) {
        this.datas = datas;
        return this;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
