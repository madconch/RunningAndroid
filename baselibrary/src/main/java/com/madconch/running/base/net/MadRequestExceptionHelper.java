package com.madconch.running.base.net;

import android.content.Context;
import android.net.ParseException;
import android.support.annotation.StringRes;

import com.google.gson.JsonParseException;
import com.madconch.running.base.R;
import com.madconch.running.base.config.BaseConfig;
import com.madconch.running.ui.loading.ILoadingHelper;
import com.madconch.running.ui.toast.MadToast;
import com.madconch.running.utillibrary.MadNetworkUtils;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * 功能描述:提供统一的错误处理内容
 * Created by LuoHaifeng on 2017/4/13.
 * Email:496349136@qq.com
 */

public class MadRequestExceptionHelper {
    public static ILoadingHelper.State getLayoutStateByThrowable(Throwable ex) {
        if (ex instanceof ConnectException || ex instanceof SocketTimeoutException || ex instanceof UnknownHostException || ex instanceof SocketException) {
            if (!MadNetworkUtils.isConnected()) {
                //如果网络未链接显示
                return ILoadingHelper.State.NO_NETWORK;
            }
        }

        return ILoadingHelper.State.ERROR;
    }

    public static String getErrorMessage(Context context, Throwable ex, @StringRes int unknownErrorMsg) {
        if (ex == null) {
            return context.getResources().getString(unknownErrorMsg);
        }

        ex.printStackTrace();

        if (ex instanceof HttpException || ex instanceof ConnectException || ex instanceof SocketTimeoutException) {
            return context.getResources().getString(R.string.network_error);
        } else if (ex instanceof CodeException) {
            CodeException cex = (CodeException) ex;
            String msg = cex.getMsg();
            return msg == null ? context.getResources().getString(R.string.network_response_code_error_default) : msg;
        } else if (ex instanceof JsonParseException
                || ex instanceof JSONException
                || ex instanceof ParseException) {
            return context.getResources().getString(R.string.network_response_parse_error);
        } else {
            return context.getResources().getString(unknownErrorMsg);
        }
    }

    public static String getErrorMessage(Throwable ex, @StringRes int unknownErrorMsg) {
        return getErrorMessage(BaseConfig.getContextProvider().provideContext(), ex, unknownErrorMsg);
    }

    public static String getErrorMessage(Context context, Throwable ex) {
        return getErrorMessage(context, ex, R.string.network_unknown_error);
    }

    public static String getErrorMessage(Throwable ex) {
        return getErrorMessage(BaseConfig.getContextProvider().provideContext(), ex);
    }

    public static void tip(Throwable ex) {
        MadToast.error(getErrorMessage(ex));
    }
}
