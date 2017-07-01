package com.rukiasoft.wildcardsproject.models;

import android.content.Context;

import com.rukiasoft.wildcardsproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roll on 29/6/17.
 */

public class PopulateUser {

    private static String[] displayNames, avatarUrls;

    public static List<User> getUsers(Context context){
        displayNames = context.getResources().getStringArray(R.array.display_names);
         avatarUrls = context.getResources().getStringArray(R.array.avatar_urls);

        List<User> users = new ArrayList<>();
        for(int i=0; i<displayNames.length; i++){
            users.add(getUser(i));
        }

        return users;
    }

    private static User getUser(int index){
        User user = new User();
        user.setPictureUrl(avatarUrls[index]);
        user.setUserName(displayNames[index]);
        user.setDateModified(System.currentTimeMillis());
        user.setCity("Villabrágima");
        user.setSmokingAttitude("muchísimo");
        user.setUserAge(27);
        user.setProfession("Cuidadora de cuquis");
        user.setWishOfChildren("Si me los cuidan...");
        return user;
    }
}
