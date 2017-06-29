package com.rukiasoft.wildcardsproject.ui;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Raúl Feliz Alonso on 6/9/15.
 */
public class ThreeTwoImageView extends android.support.v7.widget.AppCompatImageView {
    public ThreeTwoImageView(Context context) {
        super(context);
    }

    public ThreeTwoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThreeTwoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec){
        int threeTwoHeight = MeasureSpec.getSize(widthSpec) *2/3;
        int threeTwoHeightSpec = MeasureSpec.makeMeasureSpec(threeTwoHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, threeTwoHeightSpec);
    }
}
