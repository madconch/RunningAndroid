package com.madconch.running.base.helper.progress;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 功能描述:代理响应实体,支持进度
 * Created by LuoHaifeng on 2017/5/24.
 * Email:496349136@qq.com
 */

public class ProgressResponseBody extends ResponseBody {
    private ResponseBody realBody;
    private String progressListenerId;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody realBody, String progressListenerId) {
        this.realBody = realBody;
        this.progressListenerId = progressListenerId;
    }

    @Override
    public MediaType contentType() {
        return realBody.contentType();
    }

    @Override
    public long contentLength() {
        return realBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            //包装
            bufferedSource = Okio.buffer(source(realBody.source()));
        }
        return bufferedSource;
    }

    /**
     * 读取，回调进度接口
     *
     * @param source Source
     * @return Source
     */
    private Source source(Source source) {

        return new ForwardingSource(source) {
            //当前读取字节数
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                //回调，如果contentLength()不知道长度，会返回-1
                ProgressUtil.onNext(progressListenerId, new ProgressEntity().setProgress(totalBytesRead).setTotal(contentLength()).setCompleted(bytesRead == -1).setRequest(false));
                if (bytesRead == -1)
                    ProgressUtil.onComplete(progressListenerId);
                return bytesRead;
            }
        };
    }
}
