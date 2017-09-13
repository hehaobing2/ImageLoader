package com.herbib.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.herbib.imageloader.cache.ImageCache;
import com.herbib.imageloader.dataloader.DataLoader;
import com.herbib.imageloader.dataloader.LoaderFactory;
import com.herbib.imageloader.utils.StringUtils;

/**
 * 图片加载任务
 */

public class LoaderTask implements Runnable {
    private RequestQueue mQueue;
    private ImageCache mCache;
    private DataLoader mLoader;
    private boolean mStop;

    public LoaderTask(RequestQueue queue) {
        mQueue = queue;
        mCache = queue.getCache();
        mLoader = queue.getLoader();
    }

    @Override
    public void run() {
        while (!mStop) {
            try {
                ImageRequest request = mQueue.getQueue().take();
                String key = StringUtils.hashKey(request.target);
                Bitmap bitmap = mCache.get(key);
                if (bitmap == null) {
                    DataLoader loader;
                    if (mLoader != null) {
                        loader = mLoader;
                    } else {
                        loader = LoaderFactory.getLoader(request.loaderClass);
                    }
                    bitmap = loader.getBitmap(request);
                    if (bitmap != null) {
                        mCache.put(key, bitmap);
                    }
                }
                ImageDisplay.showImage((ImageView) request.view.get(), bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        mStop = false;
    }

    public void stop() {
        mStop = true;
    }
}
