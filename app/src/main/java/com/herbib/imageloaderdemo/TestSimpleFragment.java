package com.herbib.imageloaderdemo;

import android.Manifest;
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

import com.herbib.imageloader.ImageLoader;
import com.herbib.imageloader.cache.ImageCache;

import java.util.HashMap;
import java.util.Random;

import static android.content.ContentValues.TAG;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * 图片加载Demo
 */

public class TestSimpleFragment extends Fragment {
    private String[] mUrls = new String[]{
            "http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg",
            "http://img05.tooopen.com/images/20150531/tooopen_sy_127457023651.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2678372840,2244387540&fm=214&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=4195805644,827754888&fm=23&gp=0.jpg"
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
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_simple);
        showImage(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage((ImageView) v);
            }
        });
    }

    private void showImage(ImageView imageView) {
        checkPermissions();
        int index = new Random().nextInt(4);
        ImageLoader loader = ImageLoader.getInstance();
        loader.setImageCacheType(ImageCache.CacheType.DICK);
        loader.display(imageView, mUrls[index]);
        Log.d(TAG, "加载图片完成");
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
}
