package com.rukiasoft.wildcardsproject.ui;

import android.animation.Animator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rukiasoft.wildcardsproject.R;
import com.rukiasoft.wildcardsproject.models.User;
import com.rukiasoft.wildcardsproject.utilities.DisplayUtility;
import com.rukiasoft.wildcardsproject.utilities.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by etiennelawlor on 11/17/16.
 */

public class WildCardView extends FrameLayout implements View.OnTouchListener {

    // region Constants
    private static final float CARD_ROTATION_DEGREES = 40.0f;
    private static final float BADGE_ROTATION_DEGREES = 15.0f;
    private static final int DURATION = 300;
    // endregion

    // region Views
    @BindView(R.id.user_image) ImageView userImageView;
    @BindView(R.id.user_name_tv) TextView userNameTextView;
    @BindView(R.id.nope_tv) TextView nopeTextView;
    // endregion

    // region Member Variables
    private float oldX;
    private float oldY;
    private float newX;
    private float newY;
    private float dX;
    private float dY;
    private float rightBoundary;
    private float leftBoundary;
    private int screenWidth;
    private int padding;
    // endregion

    // region Constructors
    public WildCardView(Context context) {
        super(context);
        init(context, null);
    }

    public WildCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WildCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }
    // endregion

    // region View.OnTouchListener Methods
    @Override
    public boolean onTouch(final View view, MotionEvent motionEvent) {
        WildCardsStackLayout wildCardsStackLayout = ((WildCardsStackLayout)view.getParent());
        WildCardView topCard = (WildCardView) wildCardsStackLayout.getChildAt(wildCardsStackLayout.getChildCount()-1);
        if(topCard.equals(view)){
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    oldX = motionEvent.getX();
                    oldY = motionEvent.getY();

                    // cancel any current animations
                    view.clearAnimation();
                    return true;
                case MotionEvent.ACTION_UP:
                    if(isCardBeyondLeftBoundary(view)){
                        //RxBus.getInstance().send(new TopCardMovedEvent(-(screenWidth)));
                        dismissCard(view, -(screenWidth * 2));
                    } else if(isCardBeyondRightBoundary(view)){
                        //RxBus.getInstance().send(new TopCardMovedEvent(screenWidth));
                        dismissCard(view, (screenWidth * 2));
                    } else {
                        //RxBus.getInstance().send(new TopCardMovedEvent(0));
                        resetCard(view);
                    }
                    return true;
                case MotionEvent.ACTION_MOVE:
                    newX = motionEvent.getX();
                    newY = motionEvent.getY();

                    dX = newX - oldX;
                    dY = newY - oldY;

                    float posX = view.getX() + dX;

                    //RxBus.getInstance().send(new TopCardMovedEvent(posX));

                    // Set new position
                    view.setX(view.getX() + dX);
                    view.setY(view.getY() + dY);

                    setCardRotation(view, view.getX());

                    updateAlphaOfBadges(posX);
                    return true;
                default :
                    return super.onTouchEvent(motionEvent);
            }
        }
        return super.onTouchEvent(motionEvent);
    }
    // endregion

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setOnTouchListener(null);
    }

    // region Helper Methods
    private void init(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            View view = inflate(context, R.layout.wild_card, this);
// TODO: 29/6/17 hacer un bind
            ButterKnife.bind(this, view);
            //imageView = (ImageView) findViewById(R.id.iv);
            //userNameTextView = (TextView) findViewById(R.id.user_name_tv);
            //nopeTextView = (TextView) findViewById(R.id.nope_tv);

            nopeTextView.setRotation(BADGE_ROTATION_DEGREES);

            screenWidth = DisplayUtility.getScreenWidth(context);
            leftBoundary =  screenWidth * (1.0f/6.0f); // Left 1/6 of screen
            System.out.println("leftboundary" + leftBoundary);
            rightBoundary = screenWidth * (5.0f/6.0f); // Right 1/6 of screen
            System.out.println("rightBoundary" + rightBoundary);
            padding = DisplayUtility.dp2px(context, 16);

            setOnTouchListener(this);
        }
    }

    // Check if card's middle is beyond the left boundary
    private boolean isCardBeyondLeftBoundary(View view){
        System.out.println("x-l vista: " + view.getX());
        System.out.println("x-l boundary y resultado: " + (view.getWidth() / 2) + " | " + (view.getX() + (view.getWidth() / 2) < leftBoundary));
        return (view.getX() + (view.getWidth() / 2) < leftBoundary);
    }

    // Check if card's middle is beyond the right boundary
    private boolean isCardBeyondRightBoundary(View view){
        System.out.println("x-r vista: " + view.getX());
        System.out.println("x-r boundary y resultado: " + (view.getWidth() / 2) + " | " + (view.getX() + (view.getWidth() / 2) < leftBoundary));
        return (view.getX() + (view.getWidth() / 2) > rightBoundary);
    }

    private void dismissCard(final View view, int xPos){
        view.animate()
                .x(xPos)
                .y(0)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        ViewGroup viewGroup = (ViewGroup) view.getParent();
                        if(viewGroup != null) {
                            viewGroup.removeView(view);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
    }

    private void resetCard(View view){
        view.animate()
                .x(0)
                .y(0)
                .rotation(0)
                .setInterpolator(new OvershootInterpolator())
                .setDuration(DURATION);

        //likeTextView.setAlpha(0);
        nopeTextView.setAlpha(0);
    }

    private void setCardRotation(View view, float posX){
        float rotation = (CARD_ROTATION_DEGREES * (posX)) / screenWidth;
        int halfCardHeight = (view.getHeight() / 2);
        if(oldY < halfCardHeight - (2*padding)){
            view.setRotation(rotation);
        } else {
            view.setRotation(-rotation);
        }
    }

    // set alpha of like and nope badges
    private void updateAlphaOfBadges(float posX){
        float alpha = (posX - padding) / (screenWidth * 0.50f);
        //likeTextView.setAlpha(alpha);
        nopeTextView.setAlpha(-alpha);
    }

    public void bind(User user){
        if(user == null)
            return;

        setUpImage(userImageView, user);
        setUpUserName(userNameTextView, user);
        // TODO: 29/6/17 meter aquí todas las demás propiedades
    }

    private void setUpImage(ImageView iv, User user){
        String pictureUrl = user.getPictureUrl();
        if(!TextUtils.isEmpty(pictureUrl)){
            ImageUtils.loadImageFromPathInCircle(iv, pictureUrl, R.drawable.e_darling, 0);


        }
    }

    private void setUpUserName(TextView tv, User user){
        String userName = user.getUserName();
        if(!TextUtils.isEmpty(userName)){
            tv.setText(userName);
        }
    }

    private void setUpUsername(TextView tv, User user){
        /*String username = user.getUsername();
        if(!TextUtils.isEmpty(username)){
            tv.setText(username);
        }*/
    }

    // endregion
}
