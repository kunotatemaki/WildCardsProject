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
    private int screenWidth;
    private int yMultiplier;
    // endregion

    // region listeners
    AddCardListener addCardListener;
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


    public void setAddCardListener(AddCardListener addCardListener) {
        this.addCardListener = addCardListener;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        // TODO: 29/6/17 evento
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        updateChildPositions();
        addCardListener.addCard();
        // TODO: 29/6/17 evento
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // TODO: 29/6/17 suscripción???
    }

    // region Helper Methods
    private void init(){
        setClipChildren(false);

        screenWidth = DisplayUtility.getScreenWidth(getContext());
        yMultiplier = DisplayUtility.dp2px(getContext(), 8);



        setUpRxBusSubscription();
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

    private void setUpRxBusSubscription(){
        /*Subscription rxBusSubscription = RxBus.getInstance().toObserverable()
                .observeOn(AndroidSchedulers.mainThread()) // UI Thread
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if (event == null) {
                            return;
                        }

                        if(event instanceof TopCardMovedEvent){
                            float posX = ((TopCardMovedEvent) event).getPosX();

                            int childCount = getChildCount();
                            for(int i=childCount-2; i>=0; i--){
                                TinderCardView tinderCardView = (TinderCardView) getChildAt(i);

                                if(tinderCardView != null){
                                    if(Math.abs(posX) == (float)screenWidth){
                                        float scaleValue = 1 - ((childCount-2-i)/50.0f);

                                        tinderCardView.animate()
                                            .x(0)
                                            .y((childCount-2-i) * yMultiplier)
                                            .scaleX(scaleValue)
                                            .rotation(0)
                                            .setInterpolator(new AnticipateOvershootInterpolator())
                                            .setDuration(DURATION);
                                    } else {
//                                        float multiplier =  (DisplayUtility.dp2px(getContext(), 8)) / (float)screenWidth;
//                                        float dy = -(Math.abs(posX * multiplier));
//                                        tinderCard.setTranslationY(dy);
                                    }
                                }
                            }
                        }
                    }
                });

        compositeSubscription.add(rxBusSubscription);*/
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
