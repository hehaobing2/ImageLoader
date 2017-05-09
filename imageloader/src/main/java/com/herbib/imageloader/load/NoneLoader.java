package com.herbib.imageloader.load;

import android.graphics.Bitmap;

import com.herbib.imageloader.request.ImageRequest;

/**
 * 无图片加载器
 */

public class NoneLoader extends DataLoader {
    @Override
    public Bitmap getBitmap(ImageRequest request) {
        return null;
    }
}
