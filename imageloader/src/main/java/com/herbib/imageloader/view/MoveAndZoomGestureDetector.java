package com.herbib.imageloader.view;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by hehaobin on 2017/06/09.
 */

public class MoveAndZoomGestureDetector extends BaseGestureDetector {
    private OnMoveOrZoomListener mListener;
    private PointF mExtentalPointer = new PointF();
    private PointF mPrePointer;
    private PointF mCurrentPointer;

    public MoveAndZoomGestureDetector(Context context, OnMoveOrZoomListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    public void handleStartProgressEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                resetState();
                mPreEvent = MotionEvent.obtain(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mGestureProgress = mListener.onActionStart(this);
                break;
        }
    }

    @Override
    public void handleInProgressEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                resetState();
                mListener.onActionEnd(this);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                updateStateByEvent(event);
                boolean update = mListener.onAction(this);
                if (update) {
                    mPreEvent.recycle();
                    mPreEvent = MotionEvent.obtain(event);
                }
                break;
        }
    }

    @Override
    public void updateStateByEvent(MotionEvent event) {

    }

    public interface OnMoveOrZoomListener {
        boolean onActionStart(MoveAndZoomGestureDetector detector);

        boolean onZoomStart(MoveAndZoomGestureDetector detector);

        boolean onAction(MoveAndZoomGestureDetector detector);

        void onZoomEnd(MoveAndZoomGestureDetector detector);

        void onActionEnd(MoveAndZoomGestureDetector detector);
    }
}
