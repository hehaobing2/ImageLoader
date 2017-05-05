package com.herbib.imageloader.data;

import android.content.Context;
import android.util.Log;

import com.herbib.imageloader.utils.ByteUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.herbib.imageloader.Contants.TAG;

/**
 * 读取图片文件
 */

public class FileData extends ImageData {

    @Override
    public byte[] readyData(Context context, Object path) {
        Log.d(TAG, "加载存储文件图片, 目标路径：" + path);
        File file = new File((String) path);
        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "加载存储文件图片完成");
        return ByteUtils.stream2Bytes(stream);
    }
}
