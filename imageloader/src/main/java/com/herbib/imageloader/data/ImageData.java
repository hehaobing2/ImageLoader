package com.herbib.imageloader.data;

import android.content.Context;

/**
 * 图片获取数据
 */

public abstract class ImageData {
    public abstract byte[] readyData(Context context, Object path);
}
