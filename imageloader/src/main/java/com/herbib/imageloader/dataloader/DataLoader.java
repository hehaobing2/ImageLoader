package com.herbib.imageloader.dataloader;

import android.graphics.Bitmap;

import com.herbib.imageloader.ImageRequest;

/**
 * 数据加载器
 */

public abstract class DataLoader {
    public abstract Bitmap getBitmap(ImageRequest request);
}
