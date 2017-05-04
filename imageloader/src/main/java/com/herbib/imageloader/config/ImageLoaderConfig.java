package com.herbib.imageloader.config;

import com.herbib.imageloader.cache.DiskCache;
import com.herbib.imageloader.cache.DoubleCache;
import com.herbib.imageloader.cache.ImageCache;
import com.herbib.imageloader.cache.MemoryCache;
import com.herbib.imageloader.data.ImageDataLoader;
import com.herbib.imageloader.data.DownLoadData;


/**
 * 图片加载配置
 */

public class ImageLoaderConfig {
    public ImageCache imageCache = new MemoryCache();
    public ImageDataLoader imageData = new DownLoadData();
    public DisplayConfig displayConfig = new DisplayConfig();
    public int threadCount = Runtime.getRuntime().availableProcessors() / 4;

    public static class Builder {
        private final ImageLoaderConfig C;

        public Builder() {
            C = new ImageLoaderConfig();
        }

        public Builder cacheType(ImageCache.CacheType type) {
            ImageCache imageCache = C.imageCache;
            switch (type) {
                case MEMORY:
                    break;
                case DICK:
                    imageCache = new DiskCache();
                    break;
                case DOUBLE:
                    imageCache = new DoubleCache();
                    break;
                case NONE:
                    imageCache = null;
                    break;
                default:
                    break;
            }
            C.imageCache = imageCache;
            return this;
        }

        public Builder cache(ImageCache cache) {
            C.imageCache = cache;
            return this;
        }

        public Builder loadingImage(int resID) {
            C.displayConfig.loadingImageResourceID = resID;
            return this;
        }

        public Builder errorImage(int resID) {
            C.displayConfig.errorImageResourceID = resID;
            return this;
        }

        public ImageLoaderConfig build() {
            return C;
        }
    }
}
