package com.herbib.imageloader.view.bigimageview;

import android.content.Context;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.AnyThread;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 高清图加载控件
 */

public class BigImageView extends View {
    public static final String TAG = "BigImageView";
    public ReentrantReadWriteLock decoderLock = new ReentrantReadWriteLock();
    private BitmapRegionDecoder mDecoder;
    private Rect mRect;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mFullImageSampleSize;
    private int mImageWidth;
    private int mImageHeight;
    private BaseGestureDetector mDetector;
    private LinkedHashMap<Integer, List<Tile>> mTiles;
    private PointF mTranslate = new PointF();
    private PointF mCenter = new PointF();
    private Matrix mMatrix;
    private boolean mInited;

    public BigImageView(Context context) {
        super(context);
        init();
    }

    public BigImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void assets(String assets) {
        mRect = null;
        TileInitTask task = new TileInitTask(this, assets);
        task.execute();
    }

    public void onInited(BitmapRegionDecoder decoder, int imageWidth, int imageHeight) {
        if (mDecoder != null) {
            mDecoder.recycle();
        }
        mDecoder = decoder;
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;
        mCenter.set(mImageWidth / 2, mImageHeight / 2);
        mInited = true;
        if (mRect == null) {
            mRect = new Rect();
        }
        if (mImageWidth != 0 && mImageHeight != 0 && getWidth() != 0 && getHeight() != 0) {
            mRect.set(mImageWidth / 2 - getWidth() / 2
                    , mImageHeight / 2 - getHeight() / 2
                    , mImageWidth / 2 + getWidth() / 2
                    , mImageHeight / 2 + getHeight() / 2);
            debug("onMeasure baseRect:" + mRect.toString());
        } else {
            requestLayout();
        }
        invalidate();
    }

    @AnyThread
    public void debug(String message) {
        Log.d(TAG, message);
    }

    /**
     * 计算出全部Tiles
     */
    private void initTiles() {
        if (getWidth() == 0 || getHeight() == 0 || mImageWidth == 0 || mImageHeight == 0) {
            return;
        }
        mFullImageSampleSize = calculateSampleSize(mImageWidth, mImageHeight, getWidth(), getHeight());
        mFullImageSampleSize /= 2;

        if (mTiles == null) {
            mTiles = new LinkedHashMap<>();
        }
        mTiles.clear();

        int xTiles = 1;
        int yTiles = 1;
        int sampleSize = mFullImageSampleSize;
        int visWidth = (int) (getWidth() * 1.25);
        int visHeight = (int) (getHeight() * 1.25);
        while (true) {
            int tileWidth = mImageWidth / xTiles;
            int tileHeight = mImageHeight / yTiles;
            int tileWidthWithSample = tileWidth / sampleSize;
            int tileHeightWithSample = tileHeight / sampleSize;
            while (tileWidthWithSample > visWidth && sampleSize < mFullImageSampleSize) {
                xTiles++;
                tileWidth = mImageWidth / xTiles;
                tileWidthWithSample = tileWidth / sampleSize;
            }
            while (tileHeightWithSample > visHeight && sampleSize < mFullImageSampleSize) {
                yTiles++;
                tileHeight = mImageHeight / yTiles;
                tileHeightWithSample = tileHeight / sampleSize;
            }
            ArrayList<Tile> tiles = new ArrayList<>(xTiles * yTiles);
            for (int x = 0; x < xTiles; x++) {
                for (int y = 0; y < yTiles; y++) {
                    Tile tile = new Tile();
                    tile.realRect = new Rect(x * tileWidth, y * tileHeight, x == xTiles - 1 ? mImageWidth : (x + 1) * tileWidth, y == yTiles - 1 ? mImageHeight : (y + 1) * tileHeight);
                    tile.sampleSize = sampleSize;
                    tile.visRect = new Rect(0, 0, 0, 0);
                    tiles.add(tile);
                }
            }
            mTiles.put(sampleSize, tiles);
            if (sampleSize == 1) {
                break;
            } else {
                sampleSize /= 2;
            }
        }
    }

    private void init() {
        mMatrix = new Matrix();
        mDetector = new MoveGestureDetector(getContext(), new MoveGestureDetector.OnMoveListener() {
            @Override
            public boolean onMoveStart(MoveGestureDetector detector) {
                return true;
            }

            @Override
            public boolean onMoving(MoveGestureDetector detector) {
                int moveX = (int) detector.getMoveX();
                int moveY = (int) detector.getMoveY();
                if (mImageWidth > getWidth()) {
                    mRect.offset(-moveX, 0);
                    checkWidth();
                }
                if (mImageHeight > getHeight()) {
                    mRect.offset(0, -moveY);
                    checkHeight();
                }

                mTranslate.set(mRect.centerX() - mCenter.x, mRect.centerY() - mCenter.y);

                debug("onMoving mRect:" + mRect.toString()
                        + ", mRect's center:" + new PointF(mRect.centerX(), mRect.centerY()).toString()
                        + ", mTranslate:" + mTranslate.toString()
                        + ", mCenter: " + mCenter.toString());

                requestLayout();
                refreshTileVisible();
                return true;
            }

            @Override
            public void onMoveEnd(MoveGestureDetector detector) {
            }
        });
    }

    private void refreshTileVisible() {
        debug("------start-----------refreshTileVisible---------------");
        for (Map.Entry<Integer, List<Tile>> tileMap : mTiles.entrySet()) {
            for (Tile tile : tileMap.getValue()) {
                if (tileVisible(tile)) {
                    tile.visible = true;
                    refreshTileVisibleRect(tile);
                    if (tile.bitmap == null && !tile.loading) {
                        tile.loading = true;
                        TileLoadTask task = new TileLoadTask(tile, this, mDecoder);
                        task.execute();
                    } else {
                        invalidate();
                    }
                } else {
                    if (tile.bitmap != null) {
                        tile.bitmap.recycle();
                        tile.bitmap = null;
                    }
                }
            }
        }
        debug("------end-----------refreshTileVisible---------------");
    }

    private void refreshTileVisibleRect(Tile tile) {
        int left = Math.max(mRect.left, tile.realRect.left);
        int top = Math.max(mRect.top, tile.realRect.top);
        int right = Math.min(mRect.right, tile.realRect.right);
        int bottom = Math.min(mRect.bottom, tile.realRect.bottom);
        tile.visRect.set(left, top, right, bottom);
        debug("refreshTileVisibleRect realRect:" + tile.realRect.toString() + ", baseRect:" + mRect.toString() + ", visRect:" + tile.visRect.toString());
    }

    private boolean isAllTileLoad() {
        boolean isAllLoad = true;
        for (Map.Entry<Integer, List<Tile>> tileMap : mTiles.entrySet()) {
            for (Tile tile : tileMap.getValue()) {
                if (tile.loading) {
                    isAllLoad = false;
                    break;
                }
            }
            if (!isAllLoad) {
                break;
            }
        }
        debug("isAllTileLoad: " + isAllLoad);
        return isAllLoad;
    }

    public void onTileLoaded() {
        if (isAllTileLoad()) {
            invalidate();
        }
    }

    /**
     * 判断该Tile是否需要显示
     *
     * @return
     */
    private boolean tileVisible(Tile tile) {
        Rect target = tile.realRect;
        Rect base = mRect;
        boolean shouldShow = tile.sampleSize == 1
                && target.left <= base.right
                && target.bottom >= base.top
                && target.right >= base.left
                && target.top <= base.bottom;
        debug("tileVisible target:" + target.toString() + ", base:" + base.toString() + ", shouldShow:" + shouldShow);
        return shouldShow;
    }

    private int calculateSampleSize(int targetWidth, int targetHeight, int width, int height) {
        int scaleWidth = targetWidth / width;
        int scaleHeight = targetHeight / height;
        int scale = Math.max(scaleWidth, scaleHeight);
        scale = scale < 1 ? 1 : scale;
        int power = 1;
        while (power * 2 < scale) {
            power = power * 2;
        }
        return power;
    }

    private void checkWidth() {
        Rect rect = mRect;
        int imageWidth = mImageWidth;
        if (rect.right > imageWidth) {
            rect.right = imageWidth;
            rect.left = imageWidth - getWidth();
        }
        if (rect.left < 0) {
            rect.left = 0;
            rect.right = getWidth();
        }
    }

    private void checkHeight() {
        Rect rect = mRect;
        int imageHeight = mImageHeight;
        if (rect.bottom > imageHeight) {
            rect.bottom = imageHeight;
            rect.top = imageHeight - getHeight();
        }
        if (rect.top < 0) {
            rect.top = 0;
            rect.bottom = getHeight();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        boolean resizeWidth = widthSpecMode != MeasureSpec.EXACTLY;
        boolean resizeHeight = heightSpecMode != MeasureSpec.EXACTLY;
        int width, height;
        if (resizeWidth && resizeHeight) {
            width = dp2px(300);
            height = dp2px(300);
        } else if (resizeWidth) {
            width = dp2px(300);
            height = parentHeight;
        } else if (resizeHeight) {
            width = parentWidth;
            height = dp2px(300);
        } else {
            width = parentWidth;
            height = parentHeight;
        }
        setMeasuredDimension(width, height);
    }

    private int dp2px(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) (dp * metrics.density + 0.5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mInited) {
            debug("onDraw not draw without init");
            return;
        }
        if (mTiles == null) {
            initTiles();
            refreshTileVisible();
            debug("onDraw initTile and refreshTile");
        }
        debug("----------------onDraw------------start--------");
        for (Map.Entry<Integer, List<Tile>> tileMap : mTiles.entrySet()) {
            for (Tile tile : tileMap.getValue()) {
                if (tile.bitmap != null) {
                    mMatrix.reset();
                    int tranX = 0;
                    int tranY = 0;
                    debug("onDraw real:" + tile.realRect.toString() + ", vis:" + tile.visRect.toString() + ", base:" + mRect.toString());
                    mMatrix.postTranslate(tranX, tranY);
                    tranX = tile.realRect.left - mRect.left;
                    tranY = tile.realRect.top - mRect.top;
                    debug("onDraw tranX:" + tranX + ", tranY:" + tranY);
                    mMatrix.postTranslate(tranX, tranY);
                    canvas.drawBitmap(tile.bitmap, mMatrix, mPaint);
                }
            }
        }
        debug("----------------onDraw-----------end---------");
    }
}
