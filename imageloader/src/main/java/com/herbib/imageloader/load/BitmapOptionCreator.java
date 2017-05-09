package com.herbib.imageloader.load;

import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 * BitmapFactory.Options构造器
 */

public class BitmapOptionCreator {
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 400;

    public static BitmapFactory.Options getOptions(ImageView view, OnLoadBitmap onLoadBitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (onLoadBitmap == null) {
            return options;
        }
        options.inJustDecodeBounds = true;
        onLoadBitmap.onDecode(options);
        options.inSampleSize = calculateScale(options, view);
        options.inJustDecodeBounds = false;
        return options;
    }

    private static int calculateScale(BitmapFactory.Options options, ImageView view) {
        int widthScale = 0;
        int heightScale = 0;
        if (view.getWidth() == 0) {
            widthScale = options.outWidth / DEFAULT_WIDTH;
        } else {
            widthScale = options.outWidth / view.getWidth();
        }
        widthScale = widthScale < 1 ? 1 : widthScale;
        if (view.getHeight() == 0) {
            heightScale = options.outHeight / DEFAULT_HEIGHT;
        } else {
            heightScale = options.outHeight / view.getHeight();
        }
        heightScale = heightScale < 1 ? 1 : heightScale;
        return Math.max(widthScale, heightScale);
    }

    interface OnLoadBitmap {
        void onDecode(BitmapFactory.Options options);
    }
}
