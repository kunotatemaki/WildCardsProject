package com.rukiasoft.wildcardsproject.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.rukiasoft.wildcardsproject.ui.GlideCircleTransform;


public class ImageUtils {
    public static Bitmap getCircularBitmapImage(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        squaredBitmap.recycle();
        return bitmap;
    }

    public static void loadImageFromPathInCircle(ImageView imageView, String url, int defaultImage, long version) {
        Glide.with(imageView.getContext())
                .load(url)
                .centerCrop()
                .signature(new MediaStoreSignature(WildCardConstants.MIME_TYPE_PICTURE, version, 0))
                .transform(new GlideCircleTransform(imageView.getContext()))
                .skipMemoryCache( true )
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //.error(defaultImage)
                .into(imageView);
    }
}