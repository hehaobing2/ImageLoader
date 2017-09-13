package com.herbib.imageloader.dataloader;


/**
 * 加载器工厂
 */

public class LoaderFactory {
    public static final Class FILE = FileLoader.class;
    public static final Class NETWORK = NetWorkLoader.class;
    public static final Class RESOURCE = ResourceLoader.class;
    public static final Class NONE = NoneLoader.class;

    public static <T extends DataLoader> T getLoader(Class<T> loaderClass) {
        DataLoader loader = null;
        try {
            loader = loaderClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) loader;
    }
}
