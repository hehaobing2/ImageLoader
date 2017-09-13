package com.herbib.imageloader.view.bigimageview;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by hehaobin on 2017/06/08.
 */

public abstract class BaseGestureDetector {
    protected boolean mGestureProgress;
    protected MotionEvent mCurrentEvent;
    protected MotionEvent mPreEvent;
    protected Context mContext;

    public BaseGestureDetector(Context context) {
        mContext = context;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureProgress) {
            handleInProgressEvent(event);
        } else {
            handleStartProgressEvent(event);
        }
        return true;
    }

    public abstract void handleStartProgressEvent(MotionEvent event);

    public abstract void handleInProgressEvent(MotionEvent event);

    public abstract void updateStateByEvent(MotionEvent event);

    public void resetState() {
        if (mCurrentEvent != null) {
            mCurrentEvent.recycle();
            mCurrentEvent = null;
        }
        if (mPreEvent != null) {
            mPreEvent.recycle();
            mPreEvent = null;
        }
        mGestureProgress = false;
    }
}
