package com.madconch.running.mvcdemo;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.madconch.running.base.helper.progress.ProgressEntity;
import com.madconch.running.base.helper.progress.ProgressInterceptor;
import com.madconch.running.base.helper.progress.ProgressListener;
import com.madconch.running.base.helper.progress.ProgressListenerPool;
import com.madconch.running.base.widget.activity.BaseActivity;
import com.madconch.running.ui.toast.MadToast;
import com.madconch.running.utillibrary.file.MadFileUtil;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/***
 * 当需要监听请求和响应进度的时候,可以通过以下方式监听
 * 1.在OkHttpClient中添加拦截器ProgressInterceptor
 * 2.使用ProgressListenerPool.getInstance().registListener(xxx)生成监听器Wrapper
 * 3.使用Wrapper中的监听器进行监听
 * 4.使用Wrapper中的id为请求添加参数:ProgressInterceptor.LISTENER_ID_KEY = id 即可完成监听
 *
 */
public class RequestProgressActivity extends BaseActivity {
    @BindView(R.id.pb_progress_bar1)
    ProgressBar requestProgressBar;

    @BindView(R.id.pb_progress_bar2)
    ProgressBar responseProgressBar;

    OkHttpClient okHttpClient;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setTitle("RequestProgress");
        showBackButton();

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new ProgressInterceptor());
        okHttpClient = builder.build();

        File file = new File(Environment.getExternalStorageDirectory(), "xx.jpg");
        MultipartBody formBody = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), RequestBody.create(null, file))
                .build();

        ProgressListener progressListener = ProgressListenerPool.getInstance().registListener(RequestProgressActivity.this);

        String listenerId = progressListener.getId();
        progressListener.getListener()
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        MadToast.info("加载结束");
                    }
                })
                .subscribe(new Consumer<ProgressEntity>() {
                    @Override
                    public void accept(@NonNull ProgressEntity progressEntity) throws Exception {
                        System.out.println(">>>" + progressEntity.toString());
                        ProgressBar progressBar;
                        if (progressEntity.isRequest()) {
                            progressBar = requestProgressBar;
                        } else {
                            progressBar = responseProgressBar;
                        }
                        progressBar.setMax(100);
                        float percent = (float) progressEntity.getProgress() / (float) progressEntity.getTotal();
                        int progress = (int) (percent * 100);
                        progressBar.setProgress(progress);
                        if (progressEntity.isCompleted()) {
                            progressBar.setProgress(100);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                       MadToast.error("加载异常");
                    }
                });
        System.out.println(">>>poolSize:" + ProgressListenerPool.getInstance().getSize());
        Request request = new Request.Builder().url("http://103.7.28.253/download.sj.qq.com/upload/connAssitantDownload/upload/MobileAssistant_1.apk?mkey=59253c2cd4cf3655&f=6606&c=0&p=.apk&" + ProgressInterceptor.LISTENER_ID_KEY + "=" + listenerId).post(formBody).build();
//        Request request = new Request.Builder().url("http://pic55.nipic.com/file/20141208/19462408_171130083000_2.jpg?" + ProgressInterceptor.LISTENER_ID_KEY + "=" + listenerId).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response resp) throws IOException {
                MadFileUtil.writeFile(resp.body().byteStream(), MadFileUtil.getRandomFile(), true);
            }
        });
    }

    @Override
    protected View provideContentView(ViewGroup container) {
        return LayoutInflater.from(getContext()).inflate(R.layout.activity_request_progress, container, false);
    }
}
