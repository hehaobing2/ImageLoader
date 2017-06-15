package com.herbib.imageloader.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

/**
 * Created by hehaobin on 2017/06/13.
 */

public class TileLoadTask extends AsyncTask<Void, Void, Bitmap> {
    private WeakReference<Tile> mTile;
    private WeakReference<BigImageView> mView;
    private WeakReference<BitmapRegionDecoder> mDecoder;

    public TileLoadTask(Tile tile, BigImageView view, BitmapRegionDecoder decoder) {
        mTile = new WeakReference<>(tile);
        mView = new WeakReference<>(view);
        mDecoder = new WeakReference<>(decoder);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Tile tile = mTile.get();
        BigImageView view = mView.get();
        view.debug("TileLoadTask start tile:" + tile.toString());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return mDecoder.get().decodeRegion(tile.realRect, options);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mView.get().debug("TileLoadTask end");
        Tile tile = mTile.get();
        tile.loading = false;
        tile.bitmap = bitmap;
        mView.get().onTileLoaded();
    }
}
