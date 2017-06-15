package com.herbib.imageloader.view;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by hehaobin on 2017/06/13.
 */

public class Tile {
    public Bitmap bitmap;
    public boolean loading;
    public Rect realRect;
    public Rect visRect;
    public boolean visible;
    public int sampleSize;

    @Override
    public String toString() {
        return "Tile{" +
                "bitmap=" + bitmap +
                ", loading=" + loading +
                ", realRect=" + realRect +
                ", visRect=" + visRect +
                ", visible=" + visible +
                ", sampleSize=" + sampleSize +
                '}';
    }
}
