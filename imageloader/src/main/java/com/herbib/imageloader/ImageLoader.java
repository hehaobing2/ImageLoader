package com.herbib.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.herbib.imageloader.cache.DickCache;
import com.herbib.imageloader.cache.DoubleCache;
import com.herbib.imageloader.cache.ImageCache;
import com.herbib.imageloader.cache.MemoryCache;
import com.herbib.imageloader.data.ImageDataLoader;
import com.herbib.imageloader.data.ImageDownLoaderLoader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.herbib.imageloader.Contants.TAG;

/**
 * 图片加载器
 */

public class ImageLoader {
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 400;

    private static ImageLoader sInstance;
    private ImageCache mImageCache;
    private ImageDataLoader mImageDataLoader;
    private Handler mMainHandler;
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private ImageLoader() {
        mMainHandler = new Handler(Looper.getMainLooper());
        mImageCache = new MemoryCache();
        mImageDataLoader = new ImageDownLoaderLoader();
    }

    public static ImageLoader getInstance() {
        if (sInstance == null) {
            sInstance = new ImageLoader();
        }
        return sInstance;
    }

    public void setImageCache(ImageCache imageCache) {
        mImageCache = imageCache;
    }

    public void setImageDataLoader(ImageDataLoader imageDataLoader) {
        mImageDataLoader = imageDataLoader;
    }

    public void setImageCacheType(ImageCache.CacheType type) {
        switch (type) {
            case MEMORY:
                mImageCache = new MemoryCache();
                break;
            case DICK:
                mImageCache = new DickCache();
                break;
            case DOUBLE:
                mImageCache = new DoubleCache();
                break;
            default:
                break;
        }
    }

    public void display(final ImageView view, final String url) {
        view.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = mImageCache.get(url);
                Log.d(TAG, "缓存是否为空：" + (bitmap == null));
                if (bitmap == null) {
                    byte[] bytes = mImageDataLoader.readyData(url);
                    if (bytes == null || bytes.length == 0) {
                        Log.d(TAG, "下载图片大小为0");
                        return;
                    }
                    int width = view.getWidth() == 0 ? DEFAULT_WIDTH : view.getWidth();
                    int height = view.getHeight() == 0 ? DEFAULT_HEIGHT : view.getHeight();

                    Log.d(TAG, String.format("ImageView的宽：%s, 高：%s, ", width, height));
                    bitmap = readBytes(bytes, width, height);
                    Log.d(TAG, String.format("采样后的Bitmap大小是：%s", bitmap.getByteCount()));
                    mImageCache.put(url, bitmap);
                }
                showImage(view, bitmap);
            }
        });
    }

    private Bitmap readBytes(byte[] bytes, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        int widthScale = options.outWidth / width;
        int heightScale = options.outHeight / height;
        options.inSampleSize = widthScale >= heightScale ? widthScale : heightScale;
        options.inSampleSize = options.inSampleSize < 1 ? 1 : options.inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    private void showImage(final ImageView view, final Bitmap bitmap) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                view.setImageBitmap(bitmap);
            }
        });
    }
}
