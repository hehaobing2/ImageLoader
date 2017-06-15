package com.herbib.imageloader.view;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by hehaobin on 2017/06/08.
 */

public class MoveGestureDetector extends BaseGestureDetector {
    private PointF mCurrentPointer;
    private PointF mPrePointer;
    private PointF mExtenalPointer = new PointF();
    private OnMoveListener mListener;

    public MoveGestureDetector(Context context, OnMoveListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    public void handleInProgressEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mListener.onMoveEnd(this);
                resetState();
                break;
            case MotionEvent.ACTION_MOVE:
                updateStateByEvent(event);
                boolean update = mListener.onMoving(this);
                if (update) {
                    mPreEvent.recycle();
                    mPreEvent = MotionEvent.obtain(event);
                }
                break;
        }
    }

    @Override
    public void handleStartProgressEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                resetState();
                mPreEvent = MotionEvent.obtain(event);
                updateStateByEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mGestureProgress = mListener.onMoveStart(this);
                break;
        }
    }

    @Override
    public void updateStateByEvent(MotionEvent event) {
        final MotionEvent prev = mPreEvent;
        mPrePointer = calculateFocalPointer(prev);
        mCurrentPointer = calculateFocalPointer(event);
        boolean skipThisMoveEvent = prev.getPointerCount() != event.getPointerCount();
        mExtenalPointer.x = skipThisMoveEvent ? 0 : mCurrentPointer.x - mPrePointer.x;
        mExtenalPointer.y = skipThisMoveEvent ? 0 : mCurrentPointer.y - mPrePointer.y;
    }

    /**
     * 根据多点触碰计算中心点
     *
     * @param event
     * @return
     */
    private PointF calculateFocalPointer(MotionEvent event) {
        final int pointerCount = event.getPointerCount();
        float x = 0, y = 0;
        for (int i = 0; i < pointerCount; i++) {
            float pointerX = event.getX(i);
            float pointerY = event.getY(i);
            x += pointerX;
            y += pointerY;
        }
        x = x / pointerCount;
        y = y / pointerCount;
        return new PointF(x, y);
    }

    public float getMoveX() {
        return mExtenalPointer.x;
    }

    public float getMoveY() {
        return mExtenalPointer.y;
    }

    public interface OnMoveListener {
        boolean onMoveStart(MoveGestureDetector detector);

        boolean onMoving(MoveGestureDetector detector);

        void onMoveEnd(MoveGestureDetector detector);
    }
}
