package com.herbib.imageloader.policy;

import com.herbib.imageloader.request.ImageRequest;

import java.util.Comparator;

/**
 * 图片加载策略
 */

public interface LoaderPolicy extends Comparator<ImageRequest> {
}
