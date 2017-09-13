package com.herbib.imageloader.policy;

import com.herbib.imageloader.ImageRequest;

/**
 * 顺序策略，即FIFO
 */

public class SerialPolicy implements LoaderPolicy {
    @Override
    public int compare(ImageRequest o1, ImageRequest o2) {
        return o1.policyNum - o2.policyNum;
    }
}
