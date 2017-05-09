package com.herbib.imageloader.request;

import android.widget.ImageView;

import com.herbib.imageloader.load.ResourceLoader;

/**
 * 读取资源请求
 */

public class ResourceRequest extends ImageRequest<Integer> {
    public ResourceRequest(Integer target, ImageView view) {
        super(target, view, ResourceLoader.class);
    }
}
