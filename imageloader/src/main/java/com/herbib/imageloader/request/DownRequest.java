package com.herbib.imageloader.request;

import android.widget.ImageView;

import com.herbib.imageloader.load.DownLoader;

import java.net.URL;

/**
 * 下载请求
 */

public class DownRequest extends ImageRequest<URL> {
    public DownRequest(URL target, ImageView view) {
        super(target, view, DownLoader.class);
    }
}
