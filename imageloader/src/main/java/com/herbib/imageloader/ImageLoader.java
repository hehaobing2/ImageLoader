package com.herbib.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 图片加载器
 */

public class ImageLoader {
    private static final int DEFAULT_WIDH = 500;
    private static final int DEFAULT_HEIGTH = 400;

    private static ImageLoader instance;
    private LruCache<String, Bitmap> mImageCache;
    private Handler mMainHandler;
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private ImageLoader() {
        mImageCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024 / 4)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    private static byte[] stream2Bytes(InputStream inStream) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outStream.close();
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outStream.toByteArray();
    }

    public void display(final ImageView view, final String url) {
        view.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = mImageCache.get(url);
                Log.d("ImageLoader", "缓存是否为空：" + (bitmap == null));
                if (bitmap == null) {
                    byte[] bytes = download(url);
                    if (bytes == null || bytes.length == 0) {
                        Log.d("ImageLoader", "下载图片大小为0");
                        return;
                    }
                    int width = view.getWidth() == 0 ? DEFAULT_WIDH : view.getWidth();
                    int height = view.getHeight() == 0 ? DEFAULT_HEIGTH : view.getHeight();

                    Log.d("ImageLoader", String.format("ImageView的宽：%s, 高：%s, ", width, height));
                    bitmap = readBytes(bytes, width, height);
                    mImageCache.put(url, bitmap);
                }
                showImage(view, bitmap);
            }
        });
    }

    private byte[] download(String url) {
        Log.d("ImageLoader", "开始下载图片，目标地址：" + url);
        InputStream stream = null;
        byte[] bytes = null;
        try {
            URL targetUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
            stream = connection.getInputStream();
            bytes = stream2Bytes(stream);
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ImageLoader", "下载发生错误");
        }
        Log.d("ImageLoader", "下载完成");
        return bytes;
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
