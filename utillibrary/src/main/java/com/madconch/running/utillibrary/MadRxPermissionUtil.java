package com.madconch.running.utillibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 功能描述:权限请求工具
 * Created by LuoHaifeng on 2017/5/3.
 * Email:496349136@qq.com
 */

public class MadRxPermissionUtil {
    public static Observable<Boolean> requestCallPermission(Activity activity) {
        return new RxPermissions(activity)
                .request(Manifest.permission.CALL_PHONE);
    }

    public static Observable<Boolean> requestCall(final Activity activity, final String phoneNumber) {
        return MadRxPermissionUtil.requestCallPermission(activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {
                        if (granted) {
                            activity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                                    + phoneNumber)));
                        } else {
                            throw new Exception("can't get call permissions");
                        }
                    }
                });
    }
}
