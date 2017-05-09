package com.herbib.imageloader.load;


import com.herbib.imageloader.request.ImageRequest;

/**
 * 加载器工厂
 */

public class LoaderFactory {

    public static DataLoader getLoader(ImageRequest request) {
        try {
            return (DataLoader) request.loaderClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new NoneLoader();
    }
}
