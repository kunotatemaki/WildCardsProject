package com.rukiasoft.wildcardsproject.ui;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.VisibleForTesting;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rukiasoft.wildcardsproject.R;
import com.rukiasoft.wildcardsproject.models.User;
import com.rukiasoft.wildcardsproject.utilities.DisplayUtility;
import com.rukiasoft.wildcardsproject.utilities.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by roll on 29/06/17.
 */

public class WildCardView extends FrameLayout implements View.OnTouchListener {

    // region Constants
    private static final float CARD_ROTATION_DEGREES = 40.0f;
    private static final float CARD_ROTATION_Y_DEGREES = -40.0f;
    private static final float BADGE_ROTATION_DEGREES = 15.0f;
    private static final int DURATION = 300;
    private static final int DURATION_FIRST_ROTATION = 500;
    private static final int DURATION_SECOND_ROTATION = 750;
    // endregion

    // region Views to bind
    @BindView(R.id.user_image)
    ImageView userImageView;
    @BindView(R.id.user_name_tv)
    TextView userNameTextView;
    @BindView(R.id.nope_tv)
    TextView nopeTextView;
    @BindView(R.id.user_city_tv)
    TextView cityTextView;
    @BindView(R.id.work_tv)
    TextView workTextView;
    @BindView(R.id.age_tv)
    TextView ageTextView;
    @BindView(R.id.smoking_tv)
    TextView smokingTextView;
    @BindView(R.id.children_tv)
    TextView childrenTextView;
    @BindView(R.id.front_card)
    ConstraintLayout frontCard;
    @BindView(R.id.back_card)
    ConstraintLayout backCard;
    @BindView(R.id.root_cv)
    CardView rootCV;
    @BindView(R.id.flip_button)
    ImageView flipButton;
    @BindView(R.id.message_text)
    AutoCompleteTextView messageText;
    @BindView(R.id.send_button)
    FloatingActionButton sendButton;

    // endregion

    // region Member Variables
    private float oldX;
    private float oldY;
    private float rawX;
    private float rightBoundary;
    private float leftBoundary;
    private int screenWidth;
    private int padding;
    private boolean cardInactive = false;
    // endregion

    // region Constructors
    public WildCardView(Context context, boolean rotated) {
        super(context);
        init(context, rotated);
    }


    // region listeners Methods
    @Override
    public boolean onTouch(final View view, MotionEvent motionEvent) {
        WildCardsStackLayout wildCardsStackLayout = ((WildCardsStackLayout) view.getParent());
        WildCardView topCard = (WildCardView) wildCardsStackLayout.getChildAt(wildCardsStackLayout.getChildCount() - 1);
        if (topCard.equals(view)) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    oldX = motionEvent.getX();
                    oldY = motionEvent.getY();

                    // cancel any current animations
                    view.clearAnimation();
                    return true;
                case MotionEvent.ACTION_UP:
                    if (cardInactive) {
                        cardInactive = false;
                        return true;
                    }
                    if (isCardBeyondLeftBoundary(view)) {
                        dismissCard(view, -(screenWidth * 2));
                    } else {
                        resetCard(view);
                    }
                    return true;
                case MotionEvent.ACTION_MOVE:

                    if (cardInactive) {
                        return true;
                    }
                    float newX = motionEvent.getX();
                    rawX = motionEvent.getRawX();
                    float newY = motionEvent.getY();

                    float dX = newX - oldX;
                    float dY = newY - oldY;

                    float posX = view.getX() + dX;

                    if (isInDiscardSide()) {

                        // Set new position
                        view.setX(view.getX() + dX);
                        view.setY(view.getY() + dY);
                    }

                    setCardRotation(view, view.getX());

                    updateAlphaOfBadges(posX);
                    return true;
                default:
                    return super.onTouchEvent(motionEvent);
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    @OnClick(R.id.flip_button)
    public void flipButtonClick(View view){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                flipCard((View) rootCV.getParent(), true);
            }
        }, 200);
    }

    @OnClick(R.id.send_button)
    public void sendButtonClick(View view){
        messageText.setText("");
    }

    // endregion

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setOnTouchListener(null);
    }

    // region Helper Methods
    private void init(Context context, boolean rotated) {
        if (!isInEditMode()) {
            View view = inflate(context, R.layout.wild_card, this);

            ButterKnife.bind(this, view);

            nopeTextView.setRotation(BADGE_ROTATION_DEGREES);

            screenWidth = DisplayUtility.getScreenWidth(context);
            leftBoundary = screenWidth * (1.0f / 6.0f); // Left 1/6 of screen
            rightBoundary = screenWidth * (5.0f / 6.0f); // Right 1/6 of screen
            padding = DisplayUtility.dp2px(context, 16);

            if (rotated) {
                changeStatusView(frontCard);
                changeStatusView(backCard);
            }

            setOnTouchListener(this);
        }
    }



    // Check if card's middle is beyond the left boundary
    private boolean isCardBeyondLeftBoundary(View view) {
        return (view.getX() + (view.getWidth() / 2) < leftBoundary);
    }


    private void dismissCard(final View view, int xPos) {
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
                        if (viewGroup != null) {
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

    private void resetCard(View view) {
        float rotationY = view.getRotationY();
        if (rotationY > CARD_ROTATION_Y_DEGREES) {
            rotationY = 0;
        } else {
            rotationY = -180;
        }

        view.animate()
                .x(0)
                .y(0)
                .rotation(0)
                .rotationY(rotationY)
                .setInterpolator(new OvershootInterpolator())
                .setDuration(DURATION);

        nopeTextView.setAlpha(0);
    }

    private void flipCard(final View view, boolean fromButton) {
        WildCardsStackLayout wildCardsStackLayout = ((WildCardsStackLayout) view.getParent());
        wildCardsStackLayout.updateStatusTopCard();
        if(!fromButton) {
            cardInactive = true;
        }
        int duration = fromButton ? 3 * DURATION_FIRST_ROTATION : DURATION_FIRST_ROTATION;
        view.animate()
                .rotationY(-90)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(duration);


        view.setRotationY(90);
        changeStatusView(backCard);
        changeStatusView(frontCard);

        view.animate()
                .x(0)
                .y(0)
                .rotation(0)
                .rotationY(0)
                .setInterpolator(new OvershootInterpolator())
                .setDuration(DURATION_SECOND_ROTATION);


    }

    private void changeStatusView(View view) {
        int alpha = Float.valueOf(view.getAlpha()).intValue();
        alpha ^= 1;
        view.setAlpha(alpha);
        view.setVisibility(view.getVisibility() == INVISIBLE ? VISIBLE : INVISIBLE);
    }

    private void setCardRotation(View view, float posX) {
        if (isInDiscardSide()) {
            float rotation = (CARD_ROTATION_DEGREES * (posX)) / screenWidth;
            int halfCardHeight = (view.getHeight() / 2);
            if (oldY < halfCardHeight - (2 * padding)) {
                view.setRotation(rotation);
            } else {
                view.setRotation(-rotation);
            }
        } else {
            float rotation = CARD_ROTATION_Y_DEGREES * (rawX - oldX) / (rightBoundary - oldX);
            if (rotation < CARD_ROTATION_Y_DEGREES) {
                flipCard(view, false);
            } else {
                view.setRotationY(rotation);
            }
        }
    }

    private boolean isInDiscardSide() {
        return rawX <= oldX;
    }

    // set alpha of like and nope badges
    private void updateAlphaOfBadges(float posX) {
        float alpha = (posX - padding) / (screenWidth * 0.50f);
        alpha = alpha > -0.1 ? 0 : alpha;
        nopeTextView.setAlpha(-alpha);
    }

    public void bind(User user) {
        if (user == null)
            return;

        setUpImage(userImageView, user);
        setTextView(userNameTextView, user.getUserName());
        setTextView(ageTextView, String.valueOf(user.getUserAge()));
        setTextView(smokingTextView, user.getSmokingAttitude());
        setTextView(workTextView, user.getProfession());
        setTextView(childrenTextView, user.getWishOfChildren());
        setTextView(cityTextView, user.getCity());
    }

    private void setUpImage(ImageView iv, User user) {
        String pictureUrl = user.getPictureUrl();
        if (!TextUtils.isEmpty(pictureUrl)) {
            ImageUtils.loadImageFromPathInCircle(iv, pictureUrl, R.drawable.e_darling, user.getDateModified());


        }
    }

    private void setTextView(TextView tv, String text) {
        if (!TextUtils.isEmpty(text)) {
            tv.setText(text);
        }
    }


    @VisibleForTesting
    public String[] getCardTexts(){
        String[] texts =  {userNameTextView.getText().toString(),
                cityTextView.getText().toString(),
                workTextView.getText().toString(),
                ageTextView.getText().toString(),
                smokingTextView.getText().toString(),
                childrenTextView.getText().toString()
        };
        return texts;
    }

    @VisibleForTesting
    public int getFrontCardVisibility(){
        return frontCard.getVisibility();
    }

    // endregion
}
