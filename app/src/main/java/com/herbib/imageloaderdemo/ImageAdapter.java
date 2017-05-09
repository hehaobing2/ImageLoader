package com.herbib.imageloaderdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.herbib.imageloader.ImageLoader;
import com.herbib.imageloader.config.DefaultConfig;
import com.herbib.imageloader.config.LoaderConfig;

import java.util.List;

/**
 * Created by hehaobin on 2017/05/09.
 */

public class ImageAdapter extends BaseAdapter {
    private List<Object> mList;
    private LayoutInflater mInflater;
    private ImageLoader mLoader;

    public ImageAdapter(Context context, List<Object> mList) {
        this.mList = mList;
        mInflater = LayoutInflater.from(context);
        mLoader = ImageLoader.getInstance();
        LoaderConfig config = new DefaultConfig();
        mLoader.initConfig(config);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_item, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        Object target = mList.get(position);
        if (target instanceof String) {
            mLoader.display(holder.iv, (String) target);
        } else {
            mLoader.display(holder.iv, (Integer) target);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
    }
}
