package com.herbib.imageloader.request;

import android.widget.ImageView;

import com.herbib.imageloader.load.DataLoader;

import java.lang.ref.WeakReference;

/**
 * 图片加载请求
 */
public abstract class ImageRequest<T> {
    public int policyNum;
    public T target;
    public Class<DataLoader> loaderClass;
    public WeakReference<ImageView> view;

    public ImageRequest(T target, ImageView view, Class loader) {
        this.target = target;
        this.loaderClass = loader;
        this.view = new WeakReference<>(view);
    }
}
