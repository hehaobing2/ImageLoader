package com.herbib.imageloader.cache;

import android.graphics.Bitmap;

/**
 * 不缓存
 */

public class NoneCache implements ImageCache {
    @Override
    public void put(String key, Bitmap bitmap) {

    }

    @Override
    public Bitmap get(String key) {
        return null;
    }
}
