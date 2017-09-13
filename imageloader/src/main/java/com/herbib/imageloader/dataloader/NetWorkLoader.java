package com.herbib.imageloader.dataloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.herbib.imageloader.utils.BitmapOptionCreator;
import com.herbib.imageloader.ImageRequest;
import com.herbib.imageloader.utils.ByteUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络加载图片
 */

class NetWorkLoader extends DataLoader {

    @Override
    public Bitmap getBitmap(ImageRequest request) {
        if (request == null ||
                request.target == null ||
                !(request.target instanceof URL)) {
            return null;
        }
        URL url = (URL) request.target;
        Bitmap bitmap = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            //用于BitmapFactory.decodeStream()无法解析Bug
            final byte[] bytes = ByteUtils.stream2Bytes(is);
            BitmapFactory.Options options = BitmapOptionCreator.getOptions((ImageView) request.view.get(), new BitmapOptionCreator.OnLoadBitmap() {
                @Override
                public void onDecode(BitmapFactory.Options options) {
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                }
            });
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            conn.disconnect();
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
