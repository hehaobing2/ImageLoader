package com.herbib.imageloader.utils;

import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.herbib.imageloader.utils.BitmapSize;

/**
 * BitmapFactory.Options构造器
 */

public class BitmapOptionCreator {

    public static BitmapFactory.Options getOptions(ImageView view, OnLoadBitmap onLoadBitmap) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (onLoadBitmap == null) {
            return options;
        }
        options.inJustDecodeBounds = true;
        onLoadBitmap.onDecode(options);
        BitmapSize defaultSize = null;
        options.inSampleSize = calculateScale(options, view, defaultSize);
        options.inJustDecodeBounds = false;
        return options;
    }

    private static int calculateScale(BitmapFactory.Options options, ImageView view, BitmapSize defaultSize) {
        int widthScale;
        int heightScale;
        if (view.getWidth() == 0) {
            widthScale = options.outWidth / defaultSize.getWidth();
        } else {
            widthScale = options.outWidth / view.getWidth();
        }
        widthScale = widthScale < 1 ? 1 : widthScale;
        if (view.getHeight() == 0) {
            heightScale = options.outHeight / defaultSize.getHeight();
        } else {
            heightScale = options.outHeight / view.getHeight();
        }
        heightScale = heightScale < 1 ? 1 : heightScale;
        return Math.max(widthScale, heightScale);
    }

    public interface OnLoadBitmap {
        void onDecode(BitmapFactory.Options options);
    }
}
