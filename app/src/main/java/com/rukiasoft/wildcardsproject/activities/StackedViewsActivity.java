package com.rukiasoft.wildcardsproject.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rukiasoft.wildcardsproject.R;
import com.rukiasoft.wildcardsproject.models.PopulateUser;
import com.rukiasoft.wildcardsproject.models.User;
import com.rukiasoft.wildcardsproject.ui.WildCardView;
import com.rukiasoft.wildcardsproject.ui.WildCardsStackLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StackedViewsActivity extends AppCompatActivity {

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

        users = PopulateUser.getUsers(getApplicationContext());

        WildCardView tc;
        for(int i = index; index<i+STACK_SIZE; index++){
            tc = new WildCardView(this);
            tc.bind(users.get(index));
            wildCardsStackLayout.addCard(tc);
        }
    }
}
