package com.herbib.imageloader.config;

import com.herbib.imageloader.cache.ImageCache;
import com.herbib.imageloader.dataloader.DataLoader;
import com.herbib.imageloader.policy.LoaderPolicy;


/**
 * 图片加载配置
 */

public class LoaderConfig {
    public ImageCache imageCache;
    public LoaderPolicy policy;
    public DataLoader loader;
    public int threadCount;
    public int loadingImageResourceID;
    public int errorImageResourceID;

    public static class Builder {
        private final LoaderConfig C;

        public Builder() {
            C = new LoaderConfig();
        }

        public Builder cache(ImageCache cache) {
            C.imageCache = cache;
            return this;
        }

        public Builder maxThread(int count) {
            C.threadCount = count;
            return this;
        }

        public Builder loadingImage(int resID) {
            C.loadingImageResourceID = resID;
            return this;
        }

        public Builder errorImage(int resID) {
            C.errorImageResourceID = resID;
            return this;
        }

        public Builder policy(LoaderPolicy policy) {
            C.policy = policy;
            return this;
        }

        public Builder loader(DataLoader loader) {
            C.loader = loader;
            return this;
        }

        public LoaderConfig build() {
            return C;
        }
    }
}
