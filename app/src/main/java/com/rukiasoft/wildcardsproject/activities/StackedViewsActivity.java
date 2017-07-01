package com.rukiasoft.wildcardsproject.activities;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.rukiasoft.wildcardsproject.R;
import com.rukiasoft.wildcardsproject.models.PopulateUser;
import com.rukiasoft.wildcardsproject.models.User;
import com.rukiasoft.wildcardsproject.ui.CardListener;
import com.rukiasoft.wildcardsproject.ui.WildCardView;
import com.rukiasoft.wildcardsproject.ui.WildCardsStackLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.State;

public class StackedViewsActivity extends ToolbarActivity implements CardListener {

    // region Constants
    private static final int STACK_SIZE = 4;

    // endregion

    // region Views
    @BindView(R.id.wildcardstack) WildCardsStackLayout wildCardsStackLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.cards_left)
    TextView cardsLeft;
    // endregion

    // region Member Variables
    public List<User> users;
    int index;
    @State int topPicture;
    @State boolean topCardRotated;

    public StackedViewsActivity() {
    }
    // endregion

    @VisibleForTesting
    public void resetUsers(final User user) {


                users.clear();
                wildCardsStackLayout.removeAllViews();
                users.add(user);
                index = 0;
                addCard();



    }

    @VisibleForTesting
    public String[] getCardTexts(){
        WildCardView card = (WildCardView) wildCardsStackLayout.getChildAt(0);
        return card.getCardTexts();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacked_views);
        ButterKnife.bind(this);

        index = topPicture;

        this.setToolbar(mToolbar);

        wildCardsStackLayout.setCardListener(this);

        users = PopulateUser.getUsers(getApplicationContext());

        boolean rotated = topCardRotated;
        while(addWildCard(rotated)){
            rotated = false;
        }
        setCardsLeft();
    }


    private boolean addWildCard(boolean rotated){
        if(wildCardsStackLayout.getChildCount() >= STACK_SIZE || index >= users.size()){
            return false;
        }
        wildCardsStackLayout.addCard(getNextWildCard(rotated));
        return true;
    }

    private WildCardView getNextWildCard(boolean rotated){
        WildCardView tc = new WildCardView(this, rotated);
        tc.bind(users.get(index));
        index++;
        return tc;
    }

    @Override
    public void addCard() {
        addWildCard(false);
    }

    @Override
    public void decrementCounterCard() {
        topPicture++;
        setCardsLeft();
    }

    @Override
    public void updateStatusTopCard() {
        topCardRotated = !topCardRotated;
    }

    public void setCardsLeft() {
        String sCardsLeft;
        int nCardsLeft = users.size() - topPicture;
        switch (nCardsLeft){
            case 0:
                sCardsLeft = getResources().getString(R.string.no_cards_left);
                break;
            case 1:
                sCardsLeft = getResources().getString(R.string.last_card_left);
                break;
            default:
                sCardsLeft = getResources().getString(R.string.cards_left);
                break;
        }
        sCardsLeft = sCardsLeft.replace("_number_", String.valueOf(nCardsLeft));
        cardsLeft.setText(sCardsLeft);
    }

}
