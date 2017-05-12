package com.madconch.running.mvcdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madconch.running.base.widget.activity.BaseActivity;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class TestActivity extends BaseActivity {
    private Observable<String> observable;

    @Override
    protected View provideContentView(ViewGroup container) {
        return LayoutInflater.from(getContext()).inflate(R.layout.activity_test, container, false);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    public void defer(View view) {
        observable = Observable.defer(new Callable<ObservableSource<String>>() {
            @Override
            public ObservableSource<String> call() throws Exception {
                return null;
            }
        });
    }

    public void normal(View view) {
        observable = getRealObservable();
    }

    private Observable<String> getRealObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                System.out.println(">>>执行.....");
            }
        });
    }

    public void subscrible(View view) {
        System.out.println(">>>订阅.....");
        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println(">>> onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println(">>> onNext");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(">>> onError");
            }

            @Override
            public void onComplete() {
                System.out.println(">>> onComplete");
            }
        });
    }
}
