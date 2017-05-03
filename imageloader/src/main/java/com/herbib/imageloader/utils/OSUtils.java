package com.herbib.imageloader.utils;

import android.os.Build;

/**
 * 操作系统相关工具
 */

public class OSUtils {
    public static int androidVersion() {
        return Build.VERSION.SDK_INT;
    }
}
