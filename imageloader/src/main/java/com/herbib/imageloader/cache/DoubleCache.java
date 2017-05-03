package com.herbib.imageloader.cache;

import android.graphics.Bitmap;

import com.herbib.imageloader.utils.StringUtils;

/**
 * 图片双缓存
 */

public class DoubleCache implements ImageCache {
    private MemoryCache mMemoryCache;
    private DickCache mDickCache;

    public DoubleCache() {
        mMemoryCache = new MemoryCache();
        mDickCache = new DickCache();
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        String hashKey = StringUtils.string2Hash(key);
        mMemoryCache.put(hashKey, bitmap);
        mDickCache.put(hashKey, bitmap);
    }

    @Override
    public Bitmap get(String key) {
        String hashKey = StringUtils.string2Hash(key);
        Bitmap bitmap = mMemoryCache.get(hashKey);
        if (bitmap == null) {
            bitmap = mDickCache.get(hashKey);
        }
        return bitmap;
    }
}
