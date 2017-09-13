package com.herbib.imageloader.dataloader;

import android.graphics.Bitmap;

import com.herbib.imageloader.ImageRequest;

/**
 * 无图片加载器
 */

class NoneLoader extends DataLoader {
    @Override
    public Bitmap getBitmap(ImageRequest request) {
        return null;
    }
}
