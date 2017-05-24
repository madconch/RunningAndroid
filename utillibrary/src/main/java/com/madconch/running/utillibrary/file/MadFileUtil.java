package com.madconch.running.utillibrary.file;

import android.support.annotation.NonNull;

import com.madconch.running.utillibrary.MadStringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import static com.madconch.running.utillibrary.MadCacheUtil.getCacheDir;

/**
 * 功能描述:文件处理工具
 * Created by LuoHaifeng on 2017/5/24.
 * Email:496349136@qq.com
 */

public class MadFileUtil {

    /***
     * 获取随机文件名的图片文件（并在缓存目录下）
     * @return 随机图片文件
     */
    public static File getRandomPictureFile() {
        return getRandomFile(".jpg");
    }

    /**
     * 获取随机文件名的apk文件
     *
     * @return 随机apk文件
     */
    public static File getRandomApkFile() {
        return getRandomFile(".apk");
    }

    /**
     * 创建文件
     *
     * @param file 指定的文件
     * @return 是否创建成功
     */
    public static boolean createFile(File file) {
        if (file.exists()) {
            return true;
        }
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            boolean flag = file.getParentFile().mkdirs();
            if (!flag) {
                return false;
            }
        }

        try {
            boolean flag = file.createNewFile();
            if (flag) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /***
     * 获取随机文件名的无后缀格式文件
     * @return 随机文件
     */
    public static File getRandomFile() {
        File file = new File(getCacheDir(), System.currentTimeMillis() + "" + MadStringUtil.getRandomString(10));
        if (file.exists()) {
            return getRandomFile();
        }
        return file;
    }

    /**
     * 获取随机文件名的指定后缀格式的文件
     *
     * @param suffix 文件后缀
     * @return 随机文件
     */
    public static File getRandomFile(String suffix) {
        if (suffix == null) {
            return getRandomFile();
        } else {
            if (!suffix.startsWith(".")) {
                suffix = "." + suffix;
            }
        }

        File file = new File(getCacheDir(), System.currentTimeMillis() + "" + MadStringUtil.getRandomString(10) + "." + suffix);
        if (file.exists()) {
            return getRandomFile(suffix);
        }
        return file;
    }

    /**
     * 获取随机文件名（时间戳 + 随机字符串）
     *
     * @return 随机文件名
     */
    public static String getRandomFileName() {
        return System.currentTimeMillis() + "" + MadStringUtil.getRandomString(10);
    }

    /**
     * 获取文件大小
     *
     * @param file 文件
     * @return 文件大小 单位byte
     */
    public static long getFileSizeByte(File file) {
        return file.length();
    }

    /**
     * 获取文件大小
     *
     * @param file 文件
     * @return 文件大小 单位KB
     */
    public static float getFileSizeKByte(File file) {
        return (float) getFileSizeByte(file) / 1024;
    }

    /**
     * 获取文件大小
     *
     * @param file 文件
     * @return 文件大小 单位MB
     */
    public static float getFileSizeMByte(File file) {
        return getFileSizeKByte(file) / 1024;
    }

    /**
     * 获取文件大小
     *
     * @param file 文件
     * @return 文件大小 单位GB
     */
    public static float getFileSizeGByte(File file) {
        return getFileSizeMByte(file) / 1024;
    }

    /**
     * 根据文件大小自动匹配合适的单位（比如:如果文件大于1G,则使用GB单位,如果文件大于1M 则使用MB作为单位）
     *
     * @param file 指定文件
     * @return 文件大小和单位
     */
    public static FileSize getFileSize(File file) {
        if (getFileSizeGByte(file) >= 1) {
            return new FileSize().setSize(getFileSizeGByte(file)).setUnit(FileSizeUnit.UNIT_G_BYTE);
        } else if (getFileSizeMByte(file) >= 1) {
            return new FileSize().setSize(getFileSizeMByte(file)).setUnit(FileSizeUnit.UNIT_M_BYTE);
        } else if (getFileSizeKByte(file) >= 1) {
            return new FileSize().setSize(getFileSizeKByte(file)).setUnit(FileSizeUnit.UNIT_K_BYTE);
        } else {
            return new FileSize().setSize(getFileSizeByte(file)).setUnit(FileSizeUnit.UNIT_BYTE);
        }
    }

    /**
     * 根据文件大小自动匹配合适的单位（比如:如果文件大于1G,则使用GB单位,如果文件大于1M 则使用MB作为单位）
     *
     * @param file 指定文件
     * @return 文件大小(精确2位小数)+单位组成的字符串
     */
    public static String getFileSizeWithSuffix(File file) {
        FileSize fileSize = getFileSize(file);
        return String.format(Locale.CHINA, "%1$.2f %2$s", fileSize.getSize(), fileSize.getUnit().getDesc());
    }

    public static void writeFile(@NonNull final InputStream src, File dest, boolean closeSrc) throws IOException {
        boolean isSuccess = createFile(dest);
        if (!isSuccess) {
            throw new IOException("con not create file " + dest);
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int len;
            while ((len = src.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (closeSrc) {
                src.close();
            }
        }
    }
}
