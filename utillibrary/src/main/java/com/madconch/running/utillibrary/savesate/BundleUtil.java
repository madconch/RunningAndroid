package com.madconch.running.utillibrary.savesate;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;

import java.io.Serializable;

/**
 * Bundle 存放Object类型,让其自动选择存放方式
 * Created by LuoHaifeng on 2017/3/8.
 */

public class BundleUtil {
    @SuppressWarnings("unchecked")
    public static <T> T getValueFromBundle(String key, Bundle from) {
        return (T) from.get(key);
    }

    public static void putObjectToBundle(String key, Object value, Bundle target) throws Exception {
        if (value instanceof Byte) {
            target.putByte(key, (Byte) value);
        } else if (value instanceof Character) {
            target.putChar(key, (Character) value);
        } else if (value instanceof Short) {
            target.putShort(key, (Short) value);
        } else if (value instanceof Float) {
            target.putFloat(key, (Float) value);
        } else if (value instanceof CharSequence) {
            target.putCharSequence(key, (CharSequence) value);
        } else if (value instanceof Bundle) {
            target.putBundle(key, (Bundle) value);
        } else if (value instanceof Parcelable) {
            target.putParcelable(key, (Parcelable) value);
        } else if (value instanceof Size) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                target.putSize(key, (Size) value);
            } else {
                throw new Exception("not support this type:" + key + "=" + value);
            }
        } else if (value instanceof SizeF) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                target.putSizeF(key, (SizeF) value);
            } else {
                throw new Exception("not support this type:" + key + "=" + value);
            }
        } else if (value instanceof Parcelable[]) {
            target.putParcelableArray(key, (Parcelable[]) value);
        } else if (value instanceof byte[]) {
            target.putByteArray(key, (byte[]) value);
        } else if (value instanceof short[]) {
            target.putShortArray(key, (short[]) value);
        } else if (value instanceof char[]) {
            target.putCharArray(key, (char[]) value);
        } else if (value instanceof float[]) {
            target.putFloatArray(key, (float[]) value);
        } else if (value instanceof CharSequence[]) {
            target.putCharSequenceArray(key, (CharSequence[]) value);
        } else if (value instanceof IBinder) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                target.putBinder(key, (IBinder) value);
            } else {
                throw new Exception("not support this type:" + key + "=" + value);
            }
        } else if (value instanceof Serializable) {
            target.putSerializable(key, (Serializable) value);
        } else {
            throw new Exception("not support this type:" + key + "=" + value);
        }
    }
}
