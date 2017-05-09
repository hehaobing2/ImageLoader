package com.herbib.imageloader.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.herbib.imageloader.utils.StringUtils;

/**
 * 图片内存缓存
 */

public class MemoryCache implements ImageCache {
    private final LruCache<String, Bitmap> mMemoryCache;

    public MemoryCache() {
        mMemoryCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024 / 4)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在 Bitmap为ARGB_8888时，相当于getWidth() * 4 * getHeight()
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        mMemoryCache.put(StringUtils.hashKey(key), bitmap);
    }

    @Override
    public Bitmap get(String key) {
        return mMemoryCache.get(StringUtils.hashKey(key));
    }
}
