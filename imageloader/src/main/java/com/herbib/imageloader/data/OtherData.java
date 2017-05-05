package com.herbib.imageloader.data;

import android.content.Context;

/**
 * Created by hehaobin on 2017/05/05.
 */

public class OtherData extends ImageData {
    @Override
    public byte[] readyData(Context context, Object path) {
        return new byte[0];
    }
}
