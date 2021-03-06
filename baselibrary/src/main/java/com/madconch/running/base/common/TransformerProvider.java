package com.madconch.running.base.common;

import android.content.DialogInterface;
import android.support.annotation.Nullable;

import com.madconch.running.base.config.ContextProvider;
import com.madconch.running.base.helper.paging.ILifeCycleProvider;
import com.madconch.running.base.helper.paging.ILoadingProvider;
import com.madconch.running.base.helper.paging.IPagingProvider;
import com.madconch.running.base.helper.paging.IRefreshProvider;
import com.madconch.running.base.net.MadRequestExceptionHelper;
import com.madconch.running.ui.dialog.MadProgressDialog;
import com.madconch.running.ui.loading.ILoadingHelper;
import com.madconch.running.ui.toast.MadToast;
import com.madconch.running.utillibrary.file.MadFileUtil;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 功能描述:@TODO 填写功能描述
 * Created by LuoHaifeng on 2017/4/13.
 * Email:496349136@qq.com
 */

public class TransformerProvider {
//    /***
//     * 解析BaseJson类型的返回值
//     * 如果code值返回成功,则仅返回data
//     * 如果code值返回失败,则抛出CodeException,订阅者将在onError中进行处理
//     *
//     * @return
//     */
//    public static <T> Observable.Transformer<BaseJson<T>, T> provideBaseJsonParse() {
//        return new Observable.Transformer<BaseJson<T>, T>() {
//            @Override
//            public Observable<T> call(Observable<BaseJson<T>> baseJsonObservable) {
//                return baseJsonObservable.map(new Func1<BaseJson<T>, T>() {
//                    @Override
//                    public T call(BaseJson<T> tBaseJson) {
//                        if (tBaseJson.isSuccess()) {
//                            return tBaseJson.getD();
//                        }
//                        throw new CodeException(tBaseJson.getCode(), tBaseJson.getMsg());
//                    }
//                });
//            }
//        };
//    }

    public static <T extends K, K> ObservableTransformer<T, K> convertToSuperInterface() {
        return new ObservableTransformer<T, K>() {
            @Override
            public ObservableSource<K> apply(Observable<T> upstream) {
                return upstream.map(new Function<T, K>() {
                    @Override
                    public K apply(@NonNull T t) throws Exception {
                        return t;
                    }
                });
            }
        };
    }

    /**
     * 提供最基础的线程切换,订阅在子线程,响应在主线程
     *
     * @return
     */
    public static <T> ObservableTransformer<T, T> provideSchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> provideSchedulers(final ILifeCycleProvider lifeCycleProvider) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(lifeCycleProvider.<T>bindLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 提供统一的错误处理
     *
     * @return
     */
    public static <T> ObservableTransformer<T, T> provideErrorHandler() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                                MadToast.error(MadRequestExceptionHelper.getErrorMessage(throwable));
                            }
                        });
            }
        };
    }

    /***
     * 提供刷新的逻辑响应处理
     *
     * @param lifeCycle      提供生命周期
     * @param refreshProvider 提供刷新数据
     * @param retryListener  提供重试接口
     * @return
     */
    public static <T> ObservableTransformer<T, T> provideRefreshTransformer(final ILifeCycleProvider lifeCycle, final IRefreshProvider refreshProvider, final ILoadingHelper.OnRetryListener retryListener) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                if (!refreshProvider.haveData()) {
                                    refreshProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                                }
                            }
                        })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                if (!refreshProvider.haveData()) {//没有数据切换到错误布局
                                    refreshProvider.provideLoadingHelper().setRetryListener(retryListener).showState(MadRequestExceptionHelper.getLayoutStateByThrowable(throwable));
                                }
                            }
                        })
                        .doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (refreshProvider.haveData()) {//有数据显示正常布局
                                    refreshProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                                } else {//没有数据切换至空布局
                                    refreshProvider.provideLoadingHelper().setRetryListener(retryListener).showEmpty();
                                }
                            }
                        })
                        .doAfterTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                refreshProvider.provideRefreshLayout().refreshCompleted();
                                refreshProvider.provideRefreshLayout().loadMoreCompleted();
                            }
                        });
            }
        };
    }

    /***
     * 提供分页的逻辑响应处理
     *
     * @param lifeCycle      提供生命周期
     * @param pagingProvider 提供分页操作数据
     * @param retryListener  提供重试接口
     * @return
     */
    public static <T> ObservableTransformer<T, T> providePagingTransformer(final ILifeCycleProvider lifeCycle, final IPagingProvider pagingProvider, final ILoadingHelper.OnRetryListener retryListener) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                if (!pagingProvider.haveData()) {//第一页并且没有数据才替换加载中布局
                                    pagingProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                                }
                            }
                        })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                if (!pagingProvider.haveData()) {//没有数据切换到错误布局
                                    pagingProvider.provideLoadingHelper().setRetryListener(retryListener).showState(MadRequestExceptionHelper.getLayoutStateByThrowable(throwable));
                                }
                            }
                        })
                        .doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (pagingProvider.haveData()) {//有数据显示正常布局
                                    pagingProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                                } else {//没有数据切换至空布局
                                    pagingProvider.provideLoadingHelper().setRetryListener(retryListener).showEmpty();
                                }
                            }
                        })
                        .doAfterTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                pagingProvider.provideRefreshLayout().refreshCompleted();
                                pagingProvider.provideRefreshLayout().loadMoreCompleted();

                                if (pagingProvider.haveMoreData()) {
                                    pagingProvider.provideRefreshLayout().setLoadMoreEnable(true);
                                } else {
                                    pagingProvider.provideRefreshLayout().setLoadMoreEnable(false);
                                }
                            }
                        });
            }
        };
    }

    /***
     * 开始加载 显示加载中
     * 结束加载 成功  显示正常布局
     * 失败  显示失败布局
     *
     * @param lifeCycle       提供生命周期
     * @param loadingProvider 提供分页操作数据
     * @param retryListener   提供重试接口
     * @return
     */
    public static <T> ObservableTransformer<T, T> provideLoadingTransformer(final ILifeCycleProvider lifeCycle, final ILoadingProvider loadingProvider, final ILoadingHelper.OnRetryListener retryListener) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                            }
                        })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showState(MadRequestExceptionHelper.getLayoutStateByThrowable(throwable));
                            }
                        })
                        .doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                            }
                        });
            }
        };
    }

    public static <T> ObservableTransformer<T, T> provideLoadingEmptyTransformer(final ILifeCycleProvider lifeCycle, final ILoadingProvider loadingProvider, final ILoadingHelper.OnRetryListener retryListener) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                            }
                        })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showState(MadRequestExceptionHelper.getLayoutStateByThrowable(throwable));
                            }
                        })
                        .doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                if (loadingProvider.haveData()) {
                                    loadingProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                                } else {
                                    loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showEmpty();
                                }
                            }
                        });
            }
        };
    }

    public static <T> ObservableTransformer<T, T> provideBackgroundLoadingTransformer(final ILifeCycleProvider lifeCycle, final ILoadingProvider loadingProvider, final ILoadingHelper.OnRetryListener retryListener) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                if (!loadingProvider.haveData()) {
                                    loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                                }
                            }
                        })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                if (!loadingProvider.haveData()) {
                                    loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showState(MadRequestExceptionHelper.getLayoutStateByThrowable(throwable));
                                }
                            }
                        })
                        .doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                            }
                        });
            }
        };
    }

    public static <T> ObservableTransformer<T, T> provideDialogLoading(final ILifeCycleProvider lifeCycle, ContextProvider contextProvider) {
        final MadProgressDialog progressDialog = new MadProgressDialog(contextProvider.provideContext());
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull final Disposable disposable) throws Exception {
                                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        if (!disposable.isDisposed())
                                            disposable.dispose();
                                    }
                                });
                                progressDialog.show();
                            }
                        })
                        .doAfterTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                progressDialog.dismiss();
                            }
                        });
            }
        };
    }

    public static ObservableTransformer<ResponseBody, File> provideDownloadTransformer(final ILifeCycleProvider lifeCycle) {
        return provideDownloadTransformer(lifeCycle, null);
    }

    public static ObservableTransformer<ResponseBody, File> provideDownloadTransformer(final ILifeCycleProvider lifeCycle, @Nullable final String path) {
        return new ObservableTransformer<ResponseBody, File>() {
            @Override
            public ObservableSource<File> apply(Observable<ResponseBody> upstream) {
                return upstream
                        .compose(lifeCycle.<ResponseBody>bindLifecycle())
                        .compose(TransformerProvider.<ResponseBody>provideSchedulers())
                        .compose(TransformerProvider.<ResponseBody>provideErrorHandler())
                        .observeOn(Schedulers.io())
                        .flatMap(new Function<ResponseBody, ObservableSource<File>>() {
                            @Override
                            public ObservableSource<File> apply(@NonNull final ResponseBody responseBody) throws Exception {
                                return Observable.create(new ObservableOnSubscribe<File>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<File> e) throws Exception {
                                        File file;
                                        if (path == null) {
                                            file = MadFileUtil.getRandomFile();
                                        } else {
                                            file = new File(path);
                                        }

                                        MadFileUtil.writeFile(responseBody.byteStream(), file, true);
                                        e.onNext(file);
                                        e.onComplete();
                                    }
                                });
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
