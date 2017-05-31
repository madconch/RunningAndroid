package com.madconch.running.base.helper.update;

import java.io.Serializable;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/26.
 * Email:496349136@qq.com
 */

public interface IUpdateEntity extends Serializable{
    /**
     * @return 获取新版本版本号
     */
    int getVersionCode();
    /**
     * @return 获取新版本版本名称
     */
    String getVersionName();
    /**
     * @return 获取新版本更新内容
     */
    String getUpdateContent();
    /**
     * @return 是否强制更新
     */
    boolean isForceUpdate();
    /**
     * @return 新版本apk文件下载地址
     */
    String getApkDownloadUrl();
}
