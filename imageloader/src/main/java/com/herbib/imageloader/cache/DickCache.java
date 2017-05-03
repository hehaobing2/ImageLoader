package com.herbib.imageloader.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.herbib.imageloader.utils.StringUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;
import static android.os.Environment.MEDIA_MOUNTED;

/**
 * 图片文件缓存
 */

public class DickCache implements ImageCache {
    private final String mRootPath;

    public DickCache() {
        String externalStorageState = Environment.getExternalStorageState();
        StringBuilder sb = new StringBuilder();
        if (MEDIA_MOUNTED.equals(externalStorageState)) {
            sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        } else {
            sb.append(Environment.getDataDirectory().getAbsolutePath());
        }
        mRootPath = sb.append("/imagecache/").toString();
        createDir();
    }

    private void createDir() {
        File dir = new File(mRootPath);
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) {
                Log.e(TAG, "无法创建文件夹，请获取相关权限");
            }
        }
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        File file = new File(mRootPath, StringUtils.string2Hash(key) + ".jpg");
        if (file.exists()) {
            file.delete();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e(TAG, "无法创建文件，请获取相关权限！");
                e.printStackTrace();
                return;
            }
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file), 1024);
            boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            if (compress) {
                Log.d(TAG, String.format("保存图片文件成功：%s", file.getAbsolutePath()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "保存文件失败！");
        } finally {
            try {
                bos.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Bitmap get(String key) {
        StringBuilder sb = new StringBuilder()
                .append(mRootPath)
                .append(StringUtils.string2Hash(key))
                .append(".jpg");
        Log.d(TAG, String.format("读取图片文件：%s", sb.toString()));
        return BitmapFactory.decodeFile(sb.toString());
    }
}
