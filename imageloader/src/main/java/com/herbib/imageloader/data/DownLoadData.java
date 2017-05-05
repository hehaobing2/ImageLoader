package com.herbib.imageloader.data;

import android.content.Context;
import android.util.Log;

import com.herbib.imageloader.utils.ByteUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.herbib.imageloader.Contants.TAG;

/**
 * 图片文件下载
 */

public class DownLoadData extends ImageData {

    @Override
    public byte[] readyData(Context context, Object path) {
        return download((String) path);
    }

    private byte[] download(String url) {
        Log.d(TAG, "开始下载图片，目标地址：" + url);
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
