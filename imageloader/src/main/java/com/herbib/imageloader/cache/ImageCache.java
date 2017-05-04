package com.herbib.imageloader.cache;

import android.graphics.Bitmap;

/**
 * 图片缓存
 */

public interface ImageCache {
    enum CacheType {
        MEMORY,
        DICK,
        DOUBLE,
        NONE
    }
    void put(String key, Bitmap bitmap);
    Bitmap get(String key);
}
