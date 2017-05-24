package com.madconch.running.utillibrary.file;

public enum FileSizeUnit {
    UNIT_BYTE("B"),
    UNIT_K_BYTE("KB"),
    UNIT_M_BYTE("MB"),
    UNIT_G_BYTE("GB");

    private String desc;

    FileSizeUnit(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}