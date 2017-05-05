package com.madconch.running.base.common;

import com.madconch.running.base.paging.MadErrorStateHelper;
import com.madconch.running.base.paging.IDialogProvider;
import com.madconch.running.base.paging.ILifeCycleProvider;
import com.madconch.running.base.paging.ILoadingProvider;
import com.madconch.running.base.paging.IPagingProvider;
import com.madconch.running.base.paging.IRefreshProvider;
import com.madconch.running.ui.toast.MadToast;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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

    /**
     * 提供最基础的线程切换,订阅在子线程,响应在主线程
     *
     * @return
     */
    public static <T> Observable.Transformer<T, T> provideSchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> Observable.Transformer<T, T> provideSchedulers(final ILifeCycleProvider lifeCycleProvider) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
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
    public static <T> Observable.Transformer<T, T> provideErrorHandler() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                                MadToast.error(MadErrorStateHelper.getErrorMessage(throwable));
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
    public static <T> Observable.Transformer<T, T> provideRefreshTransformer(final ILifeCycleProvider lifeCycle, final IRefreshProvider refreshProvider, final RetryListener retryListener) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                if(!refreshProvider.haveData()){
                                    refreshProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                                }
                            }
                        })
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                if (!refreshProvider.haveData()) {//没有数据切换到错误布局
                                    refreshProvider.provideLoadingHelper().setRetryListener(retryListener).showState(MadErrorStateHelper.getLayoutStateByThrowable(throwable));
                                }
                            }
                        })
                        .doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                if (refreshProvider.haveData()) {//有数据显示正常布局
                                    refreshProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                                } else {//没有数据切换至空布局
                                    refreshProvider.provideLoadingHelper().setRetryListener(retryListener).showEmpty();
                                }
                            }
                        })
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                refreshProvider.getRefreshLayout().refreshCompleted();
                                refreshProvider.getRefreshLayout().loadMoreCompleted();
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
    public static <T> Observable.Transformer<T, T> providePagingTransformer(final ILifeCycleProvider lifeCycle, final IPagingProvider pagingProvider, final RetryListener retryListener) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                if (!pagingProvider.haveData()) {//第一页并且没有数据才替换加载中布局
                                    pagingProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                                }
                            }
                        })
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                if (!pagingProvider.haveData()) {//没有数据切换到错误布局
                                    pagingProvider.provideLoadingHelper().setRetryListener(retryListener).showState(MadErrorStateHelper.getLayoutStateByThrowable(throwable));
                                }
                            }
                        })
                        .doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                if (pagingProvider.haveData()) {//有数据显示正常布局
                                    pagingProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                                } else {//没有数据切换至空布局
                                    pagingProvider.provideLoadingHelper().setRetryListener(retryListener).showEmpty();
                                }
                            }
                        })
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                pagingProvider.getRefreshLayout().refreshCompleted();
                                pagingProvider.getRefreshLayout().loadMoreCompleted();

                                if (pagingProvider.haveMoreData()) {
                                    pagingProvider.getRefreshLayout().setLoadMoreEnable(true);
                                } else {
                                    pagingProvider.getRefreshLayout().setLoadMoreEnable(false);
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
    public static <T> Observable.Transformer<T, T> provideLoadingTransformer(final ILifeCycleProvider lifeCycle, final ILoadingProvider loadingProvider, final RetryListener retryListener) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                            }
                        })
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showState(MadErrorStateHelper.getLayoutStateByThrowable(throwable));
                            }
                        })
                        .doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                            }
                        });
            }
        };
    }

    public static <T> Observable.Transformer<T, T> provideLoadingEmptyTransformer(final ILifeCycleProvider lifeCycle, final ILoadingProvider loadingProvider, final RetryListener retryListener) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                            }
                        })
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showState(MadErrorStateHelper.getLayoutStateByThrowable(throwable));
                            }
                        })
                        .doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                if(loadingProvider.haveData()){
                                    loadingProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                                }else{
                                    loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showEmpty();
                                }
                            }
                        });
            }
        };
    }

    public static <T> Observable.Transformer<T, T> provideBackgroundLoadingTransformer(final ILifeCycleProvider lifeCycle, final ILoadingProvider loadingProvider, final RetryListener retryListener) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                if(!loadingProvider.haveData()){
                                    loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showLoading();
                                }
                            }
                        })
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                if(!loadingProvider.haveData()) {
                                    loadingProvider.provideLoadingHelper().setRetryListener(retryListener).showState(MadErrorStateHelper.getLayoutStateByThrowable(throwable));
                                }
                            }
                        })
                        .doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                loadingProvider.provideLoadingHelper().setRetryListener(retryListener).restore();
                            }
                        });
            }
        };
    }

    public static <T> Observable.Transformer<T, T> provideDialogLoading(final ILifeCycleProvider lifeCycle,final IDialogProvider dialogProvider) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .compose(lifeCycle.<T>bindLifecycle())
                        .compose(TransformerProvider.<T>provideSchedulers())
                        .compose(TransformerProvider.<T>provideErrorHandler())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                dialogProvider.setDialogVisible(true);
                            }
                        })
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                dialogProvider.setDialogVisible(false);
                            }
                        });
            }
        };
    }
}
