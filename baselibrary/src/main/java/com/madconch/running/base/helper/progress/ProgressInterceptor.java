package com.madconch.running.base.helper.progress;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 功能描述:请求进度处理拦截器
 * Created by LuoHaifeng on 2017/5/24.
 * Email:496349136@qq.com
 */

public class ProgressInterceptor implements Interceptor {
    public static final String LISTENER_ID_KEY = "LISTENER_ID_KEY";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String id = null;
        HttpUrl url = request.url();
        //通过query参数取进度发射器id
        if (url.queryParameterNames().contains(LISTENER_ID_KEY)) {
            id = url.queryParameter(LISTENER_ID_KEY);
            //移除该id参数,以免浪费流量
            url = url.newBuilder().addQueryParameter(LISTENER_ID_KEY, null).build();
            request = request.newBuilder().url(url).build();
        }

        try {
            Response response;
            if (request.body() == null) {
                response = chain.proceed(request);
                //get方式直接将进度更新至请求完成
                ProgressUtil.onNext(id, new ProgressEntity().setTotal(1).setProgress(1).setCompleted(true).setRequest(true));
            } else {
                //代理RequestBody,处理请求上行进度
                Request newRequest = request.newBuilder().method(request.method(), new ProgressRequestBody(request.body(), id)).build();
                response = chain.proceed(newRequest);
            }

            //代理ResponseBody,处理请求下行进度
            response = response.newBuilder().body(new ProgressResponseBody(response.body(), id)).build();
            return response;
        } catch (IOException e) {
            ProgressUtil.onError(id, e);
            throw e;
        }
    }
}
