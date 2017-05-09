package com.herbib.imageloader.request;

import android.widget.ImageView;

import com.herbib.imageloader.load.NoneLoader;

/**
 * 读取文件请求
 */

public class FileRequest extends ImageRequest<String> {

    public FileRequest(String target, ImageView view) {
        super(target, view, NoneLoader.class);
    }
}
