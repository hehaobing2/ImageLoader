package com.herbib.imageloader.policy;

import com.herbib.imageloader.ImageRequest;

import java.util.Random;

/**
 * 乱序策略
 */

public class RandomPolicy implements LoaderPolicy {
    private Random mRandom = new Random();

    @Override
    public int compare(ImageRequest o1, ImageRequest o2) {
        int power = mRandom.nextInt(2) + 1;
        return (int) Math.pow(-1, power);
    }
}
