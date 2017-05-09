package com.herbib.imageloader;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

/**
 * 图片展示
 */

public class ImageDisplay {
    private static Handler sMainHandler;

    public static void showImage(final ImageView view, final Bitmap bitmap) {
        if (sMainHandler == null) {
            sMainHandler = new Handler(Looper.getMainLooper());
        }
        sMainHandler.post(new Runnable() {
            @Override
            public void run() {
                view.setImageBitmap(bitmap);
            }
        });
    }

}
