package com.herbib.imageloader.cache;

import android.graphics.Bitmap;

import com.herbib.imageloader.utils.StringUtils;

/**
 * 图片双缓存
 */

class DoubleCache implements ImageCache {
    private MemoryCache mMemoryCache;
    private DiskCache mDiskCache;

    DoubleCache() {
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache();
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        String hashKey = StringUtils.hashKey(key);
        mMemoryCache.put(hashKey, bitmap);
        mDiskCache.put(hashKey, bitmap);
    }

    @Override
    public Bitmap get(String key) {
        String hashKey = StringUtils.hashKey(key);
        Bitmap bitmap = mMemoryCache.get(hashKey);
        if (bitmap == null) {
            bitmap = mDiskCache.get(hashKey);
        }
        return bitmap;
    }
}
