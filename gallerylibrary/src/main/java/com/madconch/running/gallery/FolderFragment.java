package com.madconch.running.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.madconch.running.base.widget.adapter.SimpleAdapter;
import com.madconch.running.base.widget.adapter.ViewHolder;

import java.io.File;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/10.
 * Email:496349136@qq.com
 */

public class FolderFragment extends Fragment {
    private RecyclerView recyclerView;
    private ISelectedFolderListener selectedFolderListener;
    private SimpleAdapter<FolderBean> folderAdapter = new SimpleAdapter<FolderBean>() {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return ViewHolder.createViewHolder(parent, R.layout.item_layout_folders);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final FolderBean folderBean = getDatas().get(position);
            final File file = new File(folderBean.getPath());
            holder.setText(R.id.tv_folder_name, file.getName());
            holder.setText(R.id.tv_folder_child_count, folderBean.getChilds().size() + "张");
            holder.setText(R.id.tv_folder_path, folderBean.getPath());
            ImageView icon = holder.getView(R.id.iv_image);

            if (folderBean.getChilds() != null && folderBean.getChilds().size() > 0) {
                Glide.with(icon.getContext()).load(folderBean.getChilds().get(0)).centerCrop().placeholder(R.mipmap.default_project_icon).error(R.mipmap.default_project_icon).into(icon);
            } else {
                icon.setImageResource(R.mipmap.default_project_icon);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedFolderListener != null) {
                        selectedFolderListener.onSelectedFolder(folderBean);
                    }
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_folders, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(folderAdapter);
        return view;
    }

    public SimpleAdapter<FolderBean> getFolderAdapter() {
        return folderAdapter;
    }

    public ISelectedFolderListener getSelectedFolderListener() {
        return selectedFolderListener;
    }

    public FolderFragment setSelectedFolderListener(ISelectedFolderListener selectedFolderListener) {
        this.selectedFolderListener = selectedFolderListener;
        return this;
    }
}
