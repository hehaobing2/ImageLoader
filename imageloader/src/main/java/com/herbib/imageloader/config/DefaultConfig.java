package com.herbib.imageloader.config;

import com.herbib.imageloader.cache.DoubleCache;
import com.herbib.imageloader.policy.ReversePolicy;

/**
 * Created by hehaobin on 2017/05/09.
 */

public class DefaultConfig extends LoaderConfig{

    public DefaultConfig() {
        imageCache = new DoubleCache();
        policy = new ReversePolicy();
        threadCount = Runtime.getRuntime().availableProcessors();
    }
}
