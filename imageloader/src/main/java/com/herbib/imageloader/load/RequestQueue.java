package com.herbib.imageloader.load;

import android.graphics.Bitmap;

import com.herbib.imageloader.ImageDisplay;
import com.herbib.imageloader.data.ImageDataFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 请求队列
 */

public class RequestQueue {

    private final int mMaxThreadCount;
    private BlockingQueue<ImageRequest> mRequestQueue;
    private ExecutorService mHandler;
    private ImageDisplay mDisplay;
    private boolean mStop;

    public RequestQueue(ImageDisplay imageDisplay) {
        this(imageDisplay, Runtime.getRuntime().availableProcessors() + 1);
    }

    public RequestQueue(ImageDisplay imageDisplay, int maxThreadCount) {
        mDisplay = imageDisplay;
        mMaxThreadCount = maxThreadCount;
        mHandler = Executors.newFixedThreadPool(maxThreadCount);
        mRequestQueue = new PriorityBlockingQueue<>();
    }

    public void start() {
        stop();
        mStop = false;
        for (int i = 0; i < mMaxThreadCount; i++) {
            mHandler.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (mStop) {
                            return;
                        }
                        ImageRequest request = mRequestQueue.take();
                        byte[] data = ImageDataFactory.getImageData(request).readyData(request.view.getContext(), request.url);
                        Bitmap bitmap = mDisplay.readBytes(data, request.view);
                        mDisplay.showImage(request.view, bitmap);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void stop(){
        mStop = true;
    }

    public void addRequest(ImageRequest request) {
        try {
            if (mRequestQueue.contains(request)) {
                mRequestQueue.remove(request);
            }
            mRequestQueue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
