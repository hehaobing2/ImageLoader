package com.herbib.imageloader.data;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.herbib.imageloader.utils.ByteUtils;

import java.io.InputStream;

import static com.herbib.imageloader.Contants.TAG;

/**
 * 读取资源文件
 */

public class ResourceData extends ImageData {
    @Override
    public byte[] readyData(Context context, Object path) {
        Log.d(TAG, "加载资源文件图片");
        Resources resources = context.getResources();
        InputStream stream = resources.openRawResource((int) path);
        Log.d(TAG, "加载资源文件图片完成");
        return ByteUtils.stream2Bytes(stream);
    }
}
