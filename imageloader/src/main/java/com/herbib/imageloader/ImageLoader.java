package com.herbib.imageloader;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.herbib.imageloader.cache.ImageCache;
import com.herbib.imageloader.config.ImageLoaderConfig;
import com.herbib.imageloader.data.ImageDataLoader;
import com.herbib.imageloader.data.DownLoadData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.herbib.imageloader.Contants.TAG;

/**
 * 图片加载器
 */

public class ImageLoader {
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 400;

    private static ImageLoader sInstance = new ImageLoader();

    private ImageLoaderConfig mConfig;
    private ImageDataLoader mImageDataLoader;
    private ImageDisplay mImageDisplay;
    private ExecutorService mExecutorService;

    private ImageLoader() {
        mConfig = new ImageLoaderConfig();
        mImageDataLoader = new DownLoadData();
        mImageDisplay = new ImageDisplay();
    }

    public static ImageLoader getInstance() {
        return sInstance;
    }

    public void config(ImageLoaderConfig config) {
        mConfig = config;
    }

    public void display(final ImageView view, final String url) {
        final ImageCache cache = mConfig.imageCache;
        if (mExecutorService == null) {
            mExecutorService = Executors.newFixedThreadPool(mConfig.threadCount);
        }
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                mImageDisplay.showImage(view, mConfig.displayConfig.loadingImageResourceID);
                Bitmap bitmap =
                        cache == null ?
                                null : cache.get(url);
                Log.d(TAG, String.format("使用%s缓存，缓存是否为%s：", mConfig.imageCache, (bitmap == null)));
                if (bitmap == null) {
                    byte[] bytes = mImageDataLoader.readyData(url);
                    if (bytes == null || bytes.length == 0) {
                        Log.d(TAG, "下载图片大小为0");
                        mImageDisplay.showImage(view, mConfig.displayConfig.errorImageResourceID);
                        return;
                    }
                    int width = view.getWidth() == 0 ? DEFAULT_WIDTH : view.getWidth();
                    int height = view.getHeight() == 0 ? DEFAULT_HEIGHT : view.getHeight();

                    Log.d(TAG, String.format("ImageView的宽：%s, 高：%s, ", width, height));
                    bitmap = mImageDisplay.readBytes(bytes, width, height);
                    Log.d(TAG, String.format("采样后的Bitmap大小是：%s", bitmap.getByteCount()));

                    if (cache != null) {
                        cache.put(url, bitmap);
                    }
                }
                mImageDisplay.showImage(view, bitmap);
            }
        });
    }
}
