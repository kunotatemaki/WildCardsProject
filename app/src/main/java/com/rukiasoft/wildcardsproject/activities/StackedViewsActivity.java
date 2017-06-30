package com.rukiasoft.wildcardsproject.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rukiasoft.wildcardsproject.R;
import com.rukiasoft.wildcardsproject.models.PopulateUser;
import com.rukiasoft.wildcardsproject.models.User;
import com.rukiasoft.wildcardsproject.ui.AddCardListener;
import com.rukiasoft.wildcardsproject.ui.WildCardView;
import com.rukiasoft.wildcardsproject.ui.WildCardsStackLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StackedViewsActivity extends AppCompatActivity implements AddCardListener {

    // region Constants
    private static final int STACK_SIZE = 4;
    // endregion

    // region Views
    @BindView(R.id.wildcardstack) WildCardsStackLayout wildCardsStackLayout;
    // endregion

    // region Member Variables
    private List<User> users;
    private int index = 0;

    public StackedViewsActivity() {
    }
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacked_views);
        ButterKnife.bind(this);

        wildCardsStackLayout.setAddCardListener(this);

        users = PopulateUser.getUsers(getApplicationContext());

        while(addWildCard()){
            addWildCard();
        }
    }

    private boolean addWildCard(){
        if(wildCardsStackLayout.getChildCount() >= STACK_SIZE || index >= users.size()){
            return false;
        }
        wildCardsStackLayout.addCard(getNextWildCard());
        return true;
    }

    private WildCardView getNextWildCard(){
        WildCardView tc = new WildCardView(this);
        tc.bind(users.get(index));
        index++;
        return tc;
    }

    @Override
    public void addCard() {
        addWildCard();
    }
}
