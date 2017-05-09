package com.herbib.imageloader.load;

import android.graphics.Bitmap;

import com.herbib.imageloader.request.ImageRequest;

/**
 * 数据加载器
 */

public abstract class DataLoader {
    public abstract Bitmap getBitmap(ImageRequest request);
}
