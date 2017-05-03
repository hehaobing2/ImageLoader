package com.herbib.imageloader.data;

import android.util.Log;

import com.herbib.imageloader.utils.ByteUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.herbib.imageloader.Contants.TAG;

/**
 * 图片文件下载
 */

public class ImageDownLoaderLoader implements ImageDataLoader {

    @Override
    public byte[] readyData(String path) {
        return download(path);
    }

    private byte[] download(String url) {
        Log.d("ImageLoader", "开始下载图片，目标地址：" + url);
        InputStream stream = null;
        byte[] bytes = null;
        try {
            URL targetUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
            stream = connection.getInputStream();
            bytes = ByteUtils.stream2Bytes(stream);
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "下载发生错误");
        }
        Log.d(TAG, "下载完成");
        return bytes;
    }
}
