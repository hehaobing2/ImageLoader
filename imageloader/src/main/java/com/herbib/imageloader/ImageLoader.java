package com.herbib.imageloader;

import android.support.annotation.StringDef;
import android.widget.ImageView;

import com.herbib.imageloader.config.LoaderConfig;
import com.herbib.imageloader.dataloader.LoaderFactory;

import java.io.File;
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

    public void display(ImageView view, String uri) {
        if (uri.startsWith("http")) {
            parseRequest(view, DataType.NETWORK, uri);
        } else {
            File file = new File(uri);
            if (file.exists() && file.isFile()) {
                display(view, file);
            }
        }
    }

    public void display(ImageView view, File localFile) {
        parseRequest(view, DataType.FILE, localFile);
    }

    public void display(ImageView view, int resourceId) {
        parseRequest(view, DataType.RESOURCE, resourceId);
    }

    private void parseRequest(ImageView view, @DataType String type, Object uri) {
        ImageRequest request = null;
        switch (type) {
            case DataType.FILE:
                try {
                    File file = (File) uri;
                    request = new ImageRequest<>(view, file, LoaderFactory.FILE);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
                break;
            case DataType.NETWORK:
                try {
                    URL url = new URL((String) uri);
                    request = new ImageRequest<>(view, url, LoaderFactory.NETWORK);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case DataType.RESOURCE:
                try {
                    Integer resourceId = (Integer) uri;
                    request = new ImageRequest<>(view, resourceId, LoaderFactory.RESOURCE);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
                break;
        }
        if (request != null) {
            mQueue.addRequest(request);
        }
    }

    public void stop() {
        mQueue.stop();
    }

    /**
     * 数据类型
     */
    @StringDef({DataType.FILE, DataType.NETWORK, DataType.RESOURCE})
    private @interface DataType {
        String FILE = "F";
        String NETWORK = "N";
        String RESOURCE = "R";
    }
}
