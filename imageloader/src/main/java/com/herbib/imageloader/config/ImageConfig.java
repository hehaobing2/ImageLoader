package com.herbib.imageloader.config;

import com.herbib.imageloader.utils.BitmapSize;

/**
 * Created by hehaobin on 2017/09/13.
 */

public class ImageConfig {
    private BitmapSize mDefaultSize;
    private int mScaleType;

    public ImageConfig(BitmapSize defaultSize, int scaleType) {
        this.mDefaultSize = defaultSize;
        this.mScaleType = scaleType;
    }
}
