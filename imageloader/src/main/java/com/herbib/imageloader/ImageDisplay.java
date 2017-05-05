package com.herbib.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import static com.herbib.imageloader.Contants.TAG;

/**
 * 图片展示
 */

public class ImageDisplay {
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 400;
    private Handler mMainHandler;

    public ImageDisplay() {
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    public void showImage(final ImageView view, final Bitmap bitmap) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                view.setImageBitmap(bitmap);
            }
        });
    }

    public Bitmap readBytes(byte[] bytes, ImageView view) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        calculateScale(view, options);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        Log.d(TAG, String.format("采样后的Bitmap大小是：%s", bitmap.getByteCount()));
        return bitmap;
    }

    private void calculateScale(ImageView view, BitmapFactory.Options options) {
        int width = view.getWidth() == 0 ? DEFAULT_WIDTH : view.getWidth();
        int height = view.getHeight() == 0 ? DEFAULT_HEIGHT : view.getHeight();
        Log.d(TAG, String.format("ImageView的宽：%s, 高：%s, ", width, height));
        int widthScale = options.outWidth / width;
        int heightScale = options.outHeight / height;
        options.inSampleSize = widthScale >= heightScale ? widthScale : heightScale;
    }

}
