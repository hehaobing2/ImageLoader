package com.herbib.imageloaderdemo;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.herbib.imageloader.view.bigimageview.BigImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * 图片加载Demo
 */

public class TestSimpleFragment extends Fragment implements View.OnClickListener {
    private Object[] mUrls = new Object[]{
            "http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg",
            "http://img05.tooopen.com/images/20150531/tooopen_sy_127457023651.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2678372840,2244387540&fm=214&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=4195805644,827754888&fm=23&gp=0.jpg",
            Integer.valueOf(R.mipmap.error),
            Integer.valueOf(R.mipmap.loading),
            Integer.valueOf(R.mipmap.picture),
            "http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg",
            "http://img05.tooopen.com/images/20150531/tooopen_sy_127457023651.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2678372840,2244387540&fm=214&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=4195805644,827754888&fm=23&gp=0.jpg",
            Integer.valueOf(R.mipmap.error),
            Integer.valueOf(R.mipmap.loading),
            Integer.valueOf(R.mipmap.picture),
            "http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg",
            "http://img05.tooopen.com/images/20150531/tooopen_sy_127457023651.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2678372840,2244387540&fm=214&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=4195805644,827754888&fm=23&gp=0.jpg",
            Integer.valueOf(R.mipmap.error),
            Integer.valueOf(R.mipmap.loading),
            Integer.valueOf(R.mipmap.picture),
            "http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg",
            "http://img05.tooopen.com/images/20150531/tooopen_sy_127457023651.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2678372840,2244387540&fm=214&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=4195805644,827754888&fm=23&gp=0.jpg",
            Integer.valueOf(R.mipmap.error),
            Integer.valueOf(R.mipmap.loading),
            Integer.valueOf(R.mipmap.picture),
    };
    private String[] mPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermissions();
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        BigImageView bigIv = (BigImageView) view.findViewById(R.id.iv_big);
        bigIv.assets("img_13896_5593.jpg");
    }

    @Override
    public void onClick(View v) {
        try {
            InputStream stream = getResources().getAssets().open("big_image.jpg");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap oldBitmap = BitmapFactory.decodeStream(stream, null, options);
            int width = options.outWidth;
            int height = options.outHeight;
            Log.d("TestSimple", "bitmap原始Size：(" + width + ", " + height + ")");
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(stream, false);
            Rect rect = new Rect(width / 2 - 100, height / 2 - 100, width / 2 + 100, height / 2 + 100);
            BitmapFactory.Options newOptions = new BitmapFactory.Options();
            Bitmap bitmap = decoder.decodeRegion(rect, newOptions);
            int newWidth = bitmap.getWidth();
            int newHeight = bitmap.getHeight();
            Log.d("TestSimple", "bitmap加载Size：(" + newWidth + ", " + newHeight + ")");
            ImageView iv = (ImageView) v;
            iv.setImageBitmap(bitmap);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            Log.d(TAG, "checkPermissions");
            for (String permission : mPermissions) {
                int has = getActivity().checkSelfPermission(permission);
                if (has != PERMISSION_GRANTED) {
                    requestPermissions(mPermissions, 101);
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            HashMap<String, String> p = new HashMap<>();
            for (int i = 0; i < permissions.length; i++) {
                p.put(permissions[i], String.valueOf(grantResults[i] == 0));
            }
            Log.d(TAG, "onRequestPermissionsResult: \n" + p.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ImageLoader.getInstance().stop();
    }
}
