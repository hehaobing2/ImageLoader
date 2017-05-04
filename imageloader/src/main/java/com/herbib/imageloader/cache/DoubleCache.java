package com.herbib.imageloader.cache;

import android.graphics.Bitmap;

import com.herbib.imageloader.utils.StringUtils;

/**
 * 图片双缓存
 */

public class DoubleCache implements ImageCache {
    private MemoryCache mMemoryCache;
    private DiskCache mDiskCache;

    public DoubleCache() {
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache();
    }

    public void put(String key, Bitmap bitmap, ImageCache.CacheType type) {
        String hashKey = StringUtils.string2Hash(key);
        mMemoryCache.put(hashKey, bitmap);
        mDiskCache.put(hashKey, bitmap);
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        put(key, bitmap, CacheType.DOUBLE);
    }

    @Override
    public Bitmap get(String key) {
        String hashKey = StringUtils.string2Hash(key);
        Bitmap bitmap = mMemoryCache.get(hashKey);
        if (bitmap == null) {
            bitmap = mDiskCache.get(hashKey);
        }
        return bitmap;
    }
}
