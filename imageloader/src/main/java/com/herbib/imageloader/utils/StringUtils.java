package com.herbib.imageloader.utils;

/**
 * 字符串相关工具
 */

public class StringUtils {

    public static String hashKey(Object key) {
        return String.valueOf(key.hashCode());
    }

}
