package com.herbib.imageloader.config;

import com.herbib.imageloader.cache.CacheFactory;
import com.herbib.imageloader.policy.ReversePolicy;

/**
 * Created by hehaobin on 2017/05/09.
 */

public class DefaultConfig extends LoaderConfig{

    public DefaultConfig() {
        imageCache = CacheFactory.getCache(CacheFactory.DOUBLE);
        policy = new ReversePolicy();
        threadCount = Runtime.getRuntime().availableProcessors();
    }
}
