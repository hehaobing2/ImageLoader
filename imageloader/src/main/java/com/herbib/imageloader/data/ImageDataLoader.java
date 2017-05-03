package com.herbib.imageloader.data;

/**
 * 图片获取数据
 */

public interface ImageDataLoader {
    byte[] readyData(String path);
}
