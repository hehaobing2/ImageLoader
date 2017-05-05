package com.herbib.imageloader.data;

import com.herbib.imageloader.load.ImageRequest;

/**
 * 图片数据工厂
 */

public class ImageDataFactory {

    public static ImageData getImageData(ImageRequest imageRequest) {
        if (imageRequest.url instanceof String) {
            String s = (String) imageRequest.url;
            if (s.startsWith("http")) {
                return getImageData(DownLoadData.class);
            } else {
                return getImageData(FileData.class);
            }
        } else {
            return getImageData(ResourceData.class);
        }
    }

    public static <T extends ImageData> T getImageData(Class<T> data) {
        ImageData loader = null;
        try {
            loader = (ImageData) Class.forName(data.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) loader;
    }
}
