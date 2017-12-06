package com.herbib.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.herbib.imageloader.cache.CacheFactory;
import com.herbib.imageloader.cache.ImageCache;
import com.herbib.imageloader.dataloader.DataLoader;
import com.herbib.imageloader.dataloader.LoaderFactory;
import com.herbib.imageloader.utils.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 图片加载任务
 */

public class LoaderExecutor implements Runnable {
    private RequestQueue mQueue;
    private ImageCache mCache;
    private DataLoader mLoader;
    private ExecutorService mExecutor;

    public LoaderExecutor(RequestQueue queue, ImageCache cache, DataLoader loader) {
        mQueue = queue;
        mExecutor = Executors.newFixedThreadPool(4);
    }

    @Override
    public void run() {
        ImageRequest request = mQueue.getRequest();
        String key = StringUtils.hashKey(request.target);
        Bitmap bitmap = loadFromCache(key, request);
        if (bitmap != null) {
            ImageDisplay.showImage((ImageView) request.view.get(), bitmap);
            return;
        }

        bitmap = loadFromLoader(request);
        if (bitmap != null) {
            mCache.put(key, bitmap);
            ImageDisplay.showImage((ImageView) request.view.get(), bitmap);
        }
    }

    private Bitmap loadFromCache(String key, ImageRequest request) {
        ImageCache cache;
        if (mCache == null
                || !mCache.getClass().equals(request.cacheClass)) {
            cache = CacheFactory.getCache(request.cacheClass);
        } else {
            cache = mCache;
        }
        return cache.get(key);
    }

    private Bitmap loadFromLoader(ImageRequest request) {
        DataLoader loader;
        if (mLoader == null
                || !mLoader.getClass().equals(request.loaderClass)) {
            loader = LoaderFactory.getLoader(request.loaderClass);
        } else {
            loader = mLoader;
        }
        return loader.getBitmap(request);
    }
}
