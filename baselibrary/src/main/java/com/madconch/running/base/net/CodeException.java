package com.madconch.running.base.net;

public class CodeException extends RuntimeException {
    private String errCode;
    private String msg;

    public CodeException(String errCode, String msg) {
        super(msg);
        this.errCode = errCode;
        this.msg = msg;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getMsg() {
        return msg;
    }
}