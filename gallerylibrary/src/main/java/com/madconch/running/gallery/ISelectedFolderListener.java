package com.madconch.running.gallery;

import java.io.Serializable;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/10.
 * Email:496349136@qq.com
 */

public interface ISelectedFolderListener extends Serializable {
    void onSelectedFolder(FolderBean folderBean);
}
