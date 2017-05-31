package com.madconch.running.base.helper.update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;

import com.madconch.running.base.common.TransformerProvider;
import com.madconch.running.base.config.MadBaseConfig;
import com.madconch.running.base.helper.paging.ILifeCycleProvider;
import com.madconch.running.base.helper.progress.ProgressEntity;
import com.madconch.running.base.helper.progress.ProgressInterceptor;
import com.madconch.running.base.helper.progress.ProgressListener;
import com.madconch.running.base.helper.progress.ProgressListenerPool;
import com.madconch.running.ui.toast.MadToast;
import com.madconch.running.utillibrary.MadSystemUtil;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static io.reactivex.Observable.create;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/5/26.
 * Email:496349136@qq.com
 */

public class UpdateManager {
    public static ObservableTransformer<IUpdateEntity, UpdateResult> provideUpdateTransformer(final Activity activity, final ILifeCycleProvider lifeCycleProvider) {
        return new ObservableTransformer<IUpdateEntity, UpdateResult>() {
            @Override
            public ObservableSource<UpdateResult> apply(Observable<IUpdateEntity> upstream) {
                return upstream
                        .compose(TransformerProvider.<IUpdateEntity>provideSchedulers(lifeCycleProvider))
                        .flatMap(new Function<IUpdateEntity, ObservableSource<UpdateResult>>() {
                            @Override
                            public ObservableSource<UpdateResult> apply(@NonNull final IUpdateEntity iUpdateEntity) throws Exception {
                                PackageManager packageManager = activity.getPackageManager();
                                PackageInfo packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
                                if (iUpdateEntity.getVersionCode() > packageInfo.versionCode) {
                                    return create(new ObservableOnSubscribe<UpdateResult>() {
                                        @Override
                                        public void subscribe(ObservableEmitter<UpdateResult> e) throws Exception {
                                            UpdateManager.showUpdateDialog(activity, lifeCycleProvider, iUpdateEntity, e);
                                        }
                                    });
                                } else {
                                    return Observable.just(new UpdateResult().setUpdateEntity(iUpdateEntity).setResult(UpdateResult.Result.RESULT_NOT_NEED_UPDATE));
                                }
                            }
                        });
            }
        };
    }

    public static void showUpdateDialog(final Activity activity, final ILifeCycleProvider lifeCycleProvider, final IUpdateEntity updateEntity, final ObservableEmitter<UpdateResult> e) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("发现新版本:" + updateEntity.getVersionName())
                .setMessage(updateEntity.getUpdateContent())
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDownloadDialog(activity, lifeCycleProvider, updateEntity, e);
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        e.onNext(new UpdateResult().setUpdateEntity(updateEntity).setResult(UpdateResult.Result.RESULT_CANCELED_UPDATE));
                        e.onComplete();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
        dialog.show();
    }

    public static void showDownloadDialog(final Activity activity, final ILifeCycleProvider lifeCycleProvider, final IUpdateEntity updateEntity, final ObservableEmitter<UpdateResult> e) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setTitle("正在更新");
        dialog.setMax(100);
        dialog.setProgress(0);
        dialog.setCancelable(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
        ProgressListener listener = ProgressListenerPool.getInstance().registListener(lifeCycleProvider);
        listener.getListener()
                .subscribe(new Consumer<ProgressEntity>() {
                    @Override
                    public void accept(@NonNull ProgressEntity progressEntity) throws Exception {
                        if (!progressEntity.isRequest()) {
                            dialog.setProgress((int) ((double) progressEntity.getProgress() / progressEntity.getTotal() * 100));
                            if (progressEntity.isCompleted()) {
                                dialog.setProgress(100);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        MadToast.error("progress error");
                    }
                });

        final Disposable disposable = provideDownloadTask(listener.getId(), updateEntity.getApkDownloadUrl(), lifeCycleProvider)
                .flatMap(new Function<File, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(@NonNull File file) throws Exception {
                        return MadSystemUtil.requestInstallApk(activity, file.getAbsolutePath(), MadBaseConfig.getFileProviderAuthority());
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        e.onNext(new UpdateResult().setUpdateEntity(updateEntity).setResult(UpdateResult.Result.RESULT_UPDATE_SUCCESS));
                        e.onComplete();
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        e.onError(throwable);
                        dialog.dismiss();
                    }
                });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(!disposable.isDisposed()){
                    disposable.dispose();
                }
                e.onNext(new UpdateResult().setUpdateEntity(updateEntity).setResult(UpdateResult.Result.RESULT_CANCELED_UPDATE));
                e.onComplete();
            }
        });
    }

    private static Observable<File> provideDownloadTask(final String progressListenerId, final String url, ILifeCycleProvider lifeCycleProvider) {
        Observable<ResponseBody> download = Observable.create(new ObservableOnSubscribe<ResponseBody>() {
            @Override
            public void subscribe(final ObservableEmitter<ResponseBody> e) throws Exception {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new ProgressInterceptor())
                        .build();
                HttpUrl httpUrl = HttpUrl.parse(url);
                httpUrl = httpUrl.newBuilder().addQueryParameter(ProgressInterceptor.LISTENER_ID_KEY, progressListenerId).build();
                okHttpClient.newCall(new Request.Builder().url(httpUrl).get().build()).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException ex) {
                        e.onError(ex);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        e.onNext(response.body());
                        e.onComplete();
                    }
                });
            }
        });

        return download.compose(TransformerProvider.provideDownloadTransformer(lifeCycleProvider));
    }
}
