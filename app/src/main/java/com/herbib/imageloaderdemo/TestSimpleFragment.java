package com.herbib.imageloaderdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.herbib.imageloader.ImageLoader;

import java.util.Random;

/**
 * Created by hehaobin on 2017/05/03.
 */

public class TestSimpleFragment extends Fragment {
    private String[] mUrls = new String[]{
            "http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg",
            "http://img05.tooopen.com/images/20150531/tooopen_sy_127457023651.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2678372840,2244387540&fm=214&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=4195805644,827754888&fm=23&gp=0.jpg"
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
        int index = new Random().nextInt(4);
        ImageLoader.getInstance().display(imageView, mUrls[index]);
    }
}
