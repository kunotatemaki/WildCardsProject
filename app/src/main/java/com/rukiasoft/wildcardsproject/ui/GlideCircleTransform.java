package com.rukiasoft.wildcardsproject.ui;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.Settings;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.rukiasoft.wildcardsproject.utilities.ImageUtils;

/**
 * Created by iRuler on 21/6/15.
 */

public class GlideCircleTransform extends BitmapTransformation {
    public GlideCircleTransform(Context context) {
        super(context);
    }
    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        return ImageUtils.getCircularBitmapImage(source);
    }
    @Override
    public String getId() {
        return "Glide_Circle_Transformation";
    }
}
