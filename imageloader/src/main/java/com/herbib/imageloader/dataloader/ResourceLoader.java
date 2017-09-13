package com.herbib.imageloader.dataloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.herbib.imageloader.utils.BitmapOptionCreator;
import com.herbib.imageloader.ImageRequest;

/**
 * 资源文件加载
 */

class ResourceLoader extends DataLoader {
    @Override
    public Bitmap getBitmap(ImageRequest request) {
        if (request == null ||
                request.target == null ||
                !(request.target instanceof Integer)) {
            return null;
        }
        Bitmap bitmap = null;
        ImageView view = (ImageView) request.view.get();
        final Integer id = (Integer) request.target;
        final Resources resources = view.getContext().getResources();
        BitmapFactory.Options options = BitmapOptionCreator.getOptions(view, new BitmapOptionCreator.OnLoadBitmap() {
            @Override
            public void onDecode(BitmapFactory.Options options) {
                BitmapFactory.decodeResource(resources, id, options);
            }
        });

        try {
            bitmap = BitmapFactory.decodeResource(resources, id, options);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
