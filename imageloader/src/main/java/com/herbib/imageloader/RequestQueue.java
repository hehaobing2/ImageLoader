package com.herbib.imageloader;

import com.herbib.imageloader.cache.ImageCache;
import com.herbib.imageloader.config.LoaderConfig;
import com.herbib.imageloader.dataloader.DataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 请求队列
 */

public class RequestQueue {

    private final int mMaxThreadCount;
    private List<LoaderTask> mTasks = new ArrayList<>();
    private BlockingQueue<ImageRequest> mRequestQueue;
    private ExecutorService mExecutor;
    private ImageCache mCache;
    private DataLoader mLoader;
    private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);

    public RequestQueue(LoaderConfig config) {
        mMaxThreadCount = config.threadCount;
        ImageCache cache = config.imageCache;
        DataLoader loader = config.loader;
        ExecutorService executor = Executors.newFixedThreadPool(mMaxThreadCount);
        BlockingQueue<ImageRequest> queue = new PriorityBlockingQueue<>(10, config.policy);

        init(cache, loader, executor, queue);
    }

    private void init(ImageCache cache, DataLoader loader, ExecutorService executor, BlockingQueue<ImageRequest> queue) {
        mCache = cache;
        mLoader = loader;
        mExecutor = executor;
        mRequestQueue = queue;
    }

    public void start() {
        for (int i = 0; i < mMaxThreadCount; i++) {
            LoaderTask runnable = new LoaderTask(this);
            mTasks.add(runnable);
            runnable.start();
            mExecutor.submit(runnable);
        }
    }

    public void addRequest(ImageRequest request) {
        try {
            if (!mRequestQueue.contains(request)) {
                request.policyNum = generateSerialNumber();
                mRequestQueue.put(request);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ImageCache getCache() {
        return mCache;
    }

    public DataLoader getLoader() {
        return mLoader;
    }

    public BlockingQueue<ImageRequest> getQueue() {
        return mRequestQueue;
    }

    public void stop() {
        for (LoaderTask run : mTasks) {
            run.stop();
        }
    }

    /**
     * 获取
     *
     * @return
     */
    private int generateSerialNumber() {
        return mSerialNumGenerator.incrementAndGet();
    }
}
