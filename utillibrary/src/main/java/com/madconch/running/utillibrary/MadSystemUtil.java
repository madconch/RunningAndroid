package com.madconch.running.utillibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;

import com.madconch.running.utillibrary.file.MadFileUtil;
import com.madconch.running.utillibrary.proxy.MadProxyUtil;
import com.madconch.running.utillibrary.proxy.ProxyResultBean;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 功能描述:权限请求工具 和 快速调用系统处理的工具类
 * Created by LuoHaifeng on 2017/5/3.
 * Email:496349136@qq.com
 */

public class MadSystemUtil {
    /**
     * 请求打电话权限
     *
     * @param activity Context实例
     * @return 可订阅者
     */
    public static Observable<Boolean> requestCallPermission(Activity activity) {
        return new RxPermissions(activity)
                .request(Manifest.permission.CALL_PHONE);
    }

    /**
     * 请求相机权限
     *
     * @param activity Context实例
     * @return 权限可订阅者
     */
    public static Observable<Boolean> requestCameraPermission(final Activity activity) {
        return new RxPermissions(activity)
                .request(Manifest.permission.CAMERA);
    }

    /***
     * 请求外部存储权限（如果是android 6.0以上新增请求Manifest.permission.READ_EXTERNAL_STORAGE）
     * @param activity Context实例
     * @return 权限可订阅者
     */
    public static Observable<Boolean> requestExternalStoragePermission(Activity activity) {
        String[] permissions;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        } else {
            permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }

        return new RxPermissions(activity).request(permissions);
    }

    /**
     * 请求拨打电话,先获取拨打权限，如果获取成功则拨打电话，否则回调错误处理程序
     *
     * @param activity    Context实例
     * @param phoneNumber 需要拨打的电话号码
     */
    public static Observable<Object> requestCall(final Activity activity, final String phoneNumber) {
        return MadSystemUtil.requestCallPermission(activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Boolean, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            activity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                                    + phoneNumber)));
                            return Observable.just(new Object());
                        } else {
                            throw new PermissionException("can't get call permissions");
                        }
                    }
                });
    }

    /**
     * 请求安装apk文件,兼容android7.0
     *
     * @param activity          Context实例
     * @param apkFilePath       apk文件路径
     * @param providerAuthority FileProvider的Authority标志
     */
    public static Observable<Object> requestInstallApk(final Activity activity, final String apkFilePath, final String providerAuthority) {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                File apkFile = new File(apkFilePath);
                if (!apkFile.exists()) {
                    e.onError(new Exception(apkFilePath + "file not exist"));
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //兼容7.0 FileUriExposedException
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(activity, providerAuthority, apkFile);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                }
                activity.startActivity(intent);
                e.onNext(true);
                e.onComplete();
            }
        });
    }

    /**
     * 请求调用系统相机进行拍照,兼容android7.0
     *
     * @param activity          Context实例
     * @param savePath          拍照文件的保存路径
     * @param providerAuthority FileProvider的Authority标志
     * @return 拍照结果的监听
     */
    public static Observable<String> requestTakePhoto(final FragmentActivity activity, final String savePath, final String providerAuthority) {
        return requestCameraPermission(activity)
                .flatMap(new Function<Boolean, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            return requestExternalStoragePermission(activity);
                        }
                        return Observable.just(false);
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<Intent>>() {
                    @Override
                    public ObservableSource<Intent> apply(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            // 激活系统的照相机进行拍照
                            Intent intent = new Intent();
                            intent.setAction("android.media.action.IMAGE_CAPTURE");
                            intent.addCategory("android.intent.category.DEFAULT");

                            //保存照片到指定的路径
                            File file = new File(savePath);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //兼容7.0 FileUriExposedException
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                Uri contentUri = FileProvider.getUriForFile(activity, providerAuthority, file);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                            } else {
                                Uri uri = Uri.fromFile(file);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            }

                            return Observable.just(intent);
                        } else {
                            throw new PermissionException("cannot get camera or storage permission");
                        }
                    }
                })
                .flatMap(new Function<Intent, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull Intent intent) throws Exception {
                        final Intent data = intent;
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                                MadProxyUtil.startActivityForResultProxy(activity, data)
                                        .subscribe(new Consumer<ProxyResultBean>() {
                                            @Override
                                            public void accept(@NonNull ProxyResultBean proxyResultBean) throws Exception {
                                                if (proxyResultBean.getResultCode() == Activity.RESULT_OK) {
                                                    e.onNext(savePath);
                                                    e.onComplete();
                                                } else {
                                                    e.onComplete();
                                                }
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(@NonNull Throwable throwable) throws Exception {
                                                e.onComplete();
                                            }
                                        });
                            }
                        });
                    }
                });
    }

    /**
     * 请求调用系统相机进行拍照,兼容android7.0,并默认在缓存目录生成一个随机名称的图片文件作为保存路径
     *
     * @param activity          Context实例
     * @param providerAuthority FileProvider的Authority标志
     * @return 拍照结果的监听
     */
    public static Observable<String> requestTakePhoto(final FragmentActivity activity, final String providerAuthority) {
        return requestTakePhoto(activity, MadFileUtil.getRandomPictureFile().getAbsolutePath(), providerAuthority);
    }
}
