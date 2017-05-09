package com.herbib.imageloader.load;

import com.herbib.imageloader.cache.ImageCache;
import com.herbib.imageloader.config.LoaderConfig;
import com.herbib.imageloader.request.ImageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 请求队列
 */

public class RequestQueue {

    private final int mMaxThreadCount;
    public List<LoaderRunnable> list = new ArrayList<>();
    private BlockingQueue<ImageRequest> mRequestQueue;
    private ExecutorService mHandler;
    private ImageCache mCache;

    public RequestQueue(LoaderConfig config) {
        mMaxThreadCount = config.threadCount;
        mCache = config.imageCache;
        mHandler = Executors.newFixedThreadPool(mMaxThreadCount);
        mRequestQueue = new PriorityBlockingQueue<>(10, config.policy);
    }

    public void start() {
        for (int i = 0; i < mMaxThreadCount; i++) {
            LoaderRunnable runnable = new LoaderRunnable(this, mCache);
            list.add(runnable);
            runnable.start();
            mHandler.submit(runnable);
        }
    }

    public BlockingQueue<ImageRequest> getQueue() {
        return mRequestQueue;
    }

    public void stop() {
        for (Controllable run : list) {
            run.stop();
        }
    }

    public void addRequest(ImageRequest request) {
        try {
            if (!mRequestQueue.contains(request)) {
                mRequestQueue.put(request);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
