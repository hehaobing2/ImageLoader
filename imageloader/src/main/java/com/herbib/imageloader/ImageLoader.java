package com.herbib.imageloader;

import android.widget.ImageView;

import com.herbib.imageloader.config.LoaderConfig;
import com.herbib.imageloader.load.RequestQueue;
import com.herbib.imageloader.request.DownRequest;
import com.herbib.imageloader.request.FileRequest;
import com.herbib.imageloader.request.ResourceRequest;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 图片加载器
 */

public class ImageLoader {
    private static ImageLoader sInstance = new ImageLoader();

    private RequestQueue mQueue;

    private ImageLoader() {
    }

    public static ImageLoader getInstance() {
        return sInstance;
    }

    public void initConfig(LoaderConfig config) {
        mQueue = new RequestQueue(config);
        mQueue.start();
    }

    public void display(ImageView view, String url) {
        if (url.startsWith("http")) {
            try {
                mQueue.addRequest(new DownRequest(new URL(url), view));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            mQueue.addRequest(new FileRequest(url, view));
        }
    }

    public void display(ImageView view, int resourceId) {
        mQueue.addRequest(new ResourceRequest(resourceId, view));
    }

    public void stop() {
        mQueue.stop();
    }

}
