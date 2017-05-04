package com.herbib.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

/**
 * 图片展示
 */

public class ImageDisplay {

    private Handler mMainHander;

    public ImageDisplay() {
        mMainHander = new Handler(Looper.getMainLooper());
    }

    public Bitmap readBytes(byte[] bytes, int width, int height) {
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

    public void showImage(final ImageView view, final Bitmap bitmap) {
        mMainHander.post(new Runnable() {
            @Override
            public void run() {
                view.setImageBitmap(bitmap);
            }
        });
    }

    public void showImage(final ImageView view, final int resID) {
        Bitmap bitmap = BitmapFactory.decodeResource(view.getResources(), resID);
        showImage(view, bitmap);
    }
}
