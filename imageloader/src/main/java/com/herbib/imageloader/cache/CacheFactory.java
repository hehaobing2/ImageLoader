package com.herbib.imageloader.cache;

/**
 * Created by hehaobin on 2017/09/13.
 */

public class CacheFactory {
    public static final Class MEMORY = MemoryCache.class;
    public static final Class DISK = DiskCache.class;
    public static final Class DOUBLE = DoubleCache.class;
    public static final Class NONE = NoneCache.class;

    public static <T extends ImageCache> T getCache(Class<T> cacheClass) {
        ImageCache cache = null;
        try {
            cache = cacheClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) cache;
    }
}
