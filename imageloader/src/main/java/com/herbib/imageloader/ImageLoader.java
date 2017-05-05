package com.herbib.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.herbib.imageloader.cache.ImageCache;
import com.herbib.imageloader.config.ImageLoaderConfig;
import com.herbib.imageloader.data.DownLoadData;
import com.herbib.imageloader.data.ImageData;
import com.herbib.imageloader.data.ImageDataFactory;
import com.herbib.imageloader.data.ResourceData;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.herbib.imageloader.Contants.TAG;

/**
 * 图片加载器
 */

public class ImageLoader {

    private static ImageLoader sInstance = new ImageLoader();

    private ImageLoaderConfig mConfig;
    private ImageData mImageData;
    private ImageCache mImageCache;
    private ImageDisplay mImageDisplay;
    private ExecutorService mExecutorService;

    private ImageLoader() {
        mConfig = new ImageLoaderConfig();
        mImageData = new DownLoadData();
        mImageDisplay = new ImageDisplay();
    }

    public static ImageLoader getInstance() {
        return sInstance;
    }

    public void config(ImageLoaderConfig config) {
        mConfig = config;
        mImageCache = config.imageCache;
    }

    public void display(final ImageView view, final String url) {
        final ImageCache cache = mConfig.imageCache;
        if (mExecutorService == null) {
            mExecutorService = Executors.newFixedThreadPool(mConfig.threadCount);
        }
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap loading = mImageDisplay.readBytes(
                        ImageDataFactory.getImageData(ResourceData.class).readyData(view.getContext(), mConfig.displayConfig.loadingImageResourceID),
                        view);
                mImageDisplay.showImage(view, loading);

                Bitmap bitmap = cache.get(url);
                Log.d(TAG, String.format("使用%s缓存，缓存是否为%s：", mConfig.imageCache, bitmap == null));
                if (bitmap == null) {
                    readyDataFactory();
                    byte[] bytes = mImageData.readyData(view.getContext(), url);
                    if (bytes == null || bytes.length == 0) {
                        Log.d(TAG, "下载图片大小为0");
                        Bitmap error = mImageDisplay.readBytes(
                                ImageDataFactory.getImageData(ResourceData.class).readyData(view.getContext(), mConfig.displayConfig.errorImageResourceID),
                                view);
                        mImageDisplay.showImage(view, error);
                        return;
                    }
                    bitmap = mImageDisplay.readBytes(bytes, view);
                    cache.put(url, bitmap);
                }

                mImageDisplay.showImage(view, bitmap);
            }
        });
    }

    private void readyDataFactory() {
        mImageData = ImageDataFactory.getImageData(DownLoadData.class);
    }
}
