package com.herbib.imageloader.view.bigimageview;

import android.content.Context;
import android.graphics.BitmapRegionDecoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by hehaobin on 2017/06/13.
 */

public class TileInitTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<BigImageView> mView;
    private WeakReference<Context> mContext;
    private String mAssets;
    private int[] mSize = new int[2];
    private BitmapRegionDecoder mDecoder;

    public TileInitTask(BigImageView view, String assets) {
        mView = new WeakReference<>(view);
        mAssets = assets;
        mContext = new WeakReference<>(view.getContext());
    }

    @Override
    protected Void doInBackground(Void... params) {
        InputStream stream = null;
        try {
            stream = mContext.get().getResources().getAssets().open(mAssets);
            BigImageView view = mView.get();
            mDecoder = BitmapRegionDecoder.newInstance(stream, false);
            mSize[0] = mDecoder.getWidth();
            mSize[1] = mDecoder.getHeight();
            view.debug("setInputStream——原始ImageSize：(" + mSize[0] + ", " + mSize[1] + ")");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mView.get().onInited(mDecoder, mSize[0], mSize[1]);
    }
}
