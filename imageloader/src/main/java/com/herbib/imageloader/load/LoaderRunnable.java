package com.herbib.imageloader.load;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.herbib.imageloader.ImageDisplay;
import com.herbib.imageloader.cache.ImageCache;
import com.herbib.imageloader.request.ImageRequest;
import com.herbib.imageloader.utils.StringUtils;

/**
 * 图片加载任务
 */

public class LoaderRunnable implements Runnable, Controllable {
    private RequestQueue mQueue;
    private ImageCache mCache;
    private boolean mStop;

    public LoaderRunnable(RequestQueue queue, ImageCache cache) {
        mQueue = queue;
        mCache = cache;
    }

    @Override
    public void run() {
        while (!mStop) {
            try {
                ImageRequest request = mQueue.getQueue().take();
                String key = StringUtils.hashKey(request.target);
                Bitmap bitmap = mCache.get(key);
                if (bitmap == null) {
                    bitmap = LoaderFactory.getLoader(request).getBitmap(request);
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

    @Override
    public void start() {
        mStop = false;
    }

    @Override
    public void stop() {
        mStop = true;
    }
}
