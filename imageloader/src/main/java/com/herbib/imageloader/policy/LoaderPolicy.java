package com.herbib.imageloader.policy;

import com.herbib.imageloader.ImageRequest;

import java.util.Comparator;

/**
 * 图片加载策略
 */

public interface LoaderPolicy extends Comparator<ImageRequest> {
}
