package com.herbib.imageloader;

import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * 图片加载请求
 * @param <T> 目标路径的数据类型
 */
public class ImageRequest<T> {
    public int policyNum;
    public T target;
    public Class loaderClass;
    public WeakReference<ImageView> view;

    public ImageRequest(ImageView view, T target, Class loader) {
        this.target = target;
        this.loaderClass = loader;
        this.view = new WeakReference<>(view);
    }
}
