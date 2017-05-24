package com.madconch.running.base.helper.progress;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 功能描述:代理请求实体,支持进度
 * Created by LuoHaifeng on 2017/5/24.
 * Email:496349136@qq.com
 */

public class ProgressRequestBody extends RequestBody {
    private RequestBody realBody;
    private String progressListenerId;
    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody realBody, String progressListenerId) {
        this.realBody = realBody;
        this.progressListenerId = progressListenerId;
    }

    @Override
    public MediaType contentType() {
        return realBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            //包装
            bufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        realBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    @Override
    public long contentLength() throws IOException {
        return realBody.contentLength();
    }

    private Sink sink(final Sink src) {
        return new ForwardingSink(src) {
            long bytesWritten = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                bytesWritten += byteCount;
                ProgressUtil.onNext(progressListenerId, new ProgressEntity().setProgress(bytesWritten).setTotal(contentLength()).setCompleted(bytesWritten == contentLength()).setRequest(true));
            }
        };
    }
}
