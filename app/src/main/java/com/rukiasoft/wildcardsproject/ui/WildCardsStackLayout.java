package com.rukiasoft.wildcardsproject.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;

import com.rukiasoft.wildcardsproject.utilities.DisplayUtility;


/**
 * Created by roll on 26/06/17.
 */

public class WildCardsStackLayout extends FrameLayout {

    // region Constants
    private static final int DURATION = 300;
    // endregion

    // region Member Variables
    private int yMultiplier;
    // endregion

    // region listeners
    SwipeCardListener addCardListener;
    // endregion

    // region Constructors
    public WildCardsStackLayout(Context context) {
        super(context);
        init();
    }

    public WildCardsStackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WildCardsStackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    // endregion


    public void setAddCardListener(SwipeCardListener addCardListener) {
        this.addCardListener = addCardListener;
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        updateChildPositions();
        addCardListener.addCard();
        addCardListener.decrementCounterCard();
    }


    // region Helper Methods
    private void init(){
        setClipChildren(false);

        yMultiplier = DisplayUtility.dp2px(getContext(), 8);


    }

    public void updateChildPositions(){
        int childCount = getChildCount();
        for(int i=childCount-1; i>=0; i--) {
            WildCardView wildCardView = (WildCardView) getChildAt(i);

            if (wildCardView != null) {

                float scaleValue = 1 - ((childCount - 2 - i) / 50.0f);

                wildCardView.animate()
                        .x(0)
                        .y((childCount - 1 - i) * yMultiplier)
                        .scaleX(scaleValue)
                        .rotation(0)
                        .setInterpolator(new AnticipateOvershootInterpolator())
                        .setDuration(DURATION);
            }
        }
    }


    public void addCard(WildCardView tc){
        ViewGroup.LayoutParams layoutParams; layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int childCount = getChildCount();
        addView(tc, 0, layoutParams);

        float scaleValue = 1 - (childCount/50.0f);

        tc.animate()
            .x(0)
            .y(childCount * yMultiplier)
            .scaleX(scaleValue)
            .setInterpolator(new AnticipateOvershootInterpolator())
            .setDuration(DURATION);
    }


    // endregion
}
