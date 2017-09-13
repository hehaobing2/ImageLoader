package com.herbib.imageloader.policy;

import com.herbib.imageloader.ImageRequest;

/**
 * 逆序策略，即LIFO
 */

public class ReversePolicy implements LoaderPolicy {
    @Override
    public int compare(ImageRequest o1, ImageRequest o2) {
        return o2.policyNum - o1.policyNum;
    }
}
