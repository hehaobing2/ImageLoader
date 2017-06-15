package com.herbib.imageloaderdemo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;

/**
 * Created by hehaobin on 2017/05/15.
 */

public class AlphaDrawable extends BitmapDrawable {
    private static final float FADE_DURATION = 3000F; //ms
    private boolean animating = false;
    private long startTimeMillis;
    int alpha = 0xFF;

    public AlphaDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
        this.animating = true;
        this.startTimeMillis = SystemClock.uptimeMillis();
    }

    @Override
    public void draw(Canvas canvas) {
        if (!animating) {
            super.draw(canvas);
        } else {
            float normalized = (SystemClock.uptimeMillis() - startTimeMillis) / FADE_DURATION;
            if (normalized >= 1.0f) {
                animating = false;
                super.draw(canvas);
            } else {
                int partialAlpha = (int) (alpha * normalized);
                super.setAlpha(partialAlpha);
                super.draw(canvas);
            }
        }
    }
}
