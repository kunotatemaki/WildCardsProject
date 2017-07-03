package com.rukiasoft.wildcardsproject.models;

import android.content.Context;

import com.rukiasoft.wildcardsproject.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Roll on 29/6/17.
 */

public class PopulateUser {

    private static String[] displayNames, avatarUrls, professions, smoking, children, cities;
    private static int[] ages;

    public static List<User> getUsers(Context context){
        displayNames = context.getResources().getStringArray(R.array.display_names);
        avatarUrls = context.getResources().getStringArray(R.array.avatar_urls);
        cities = context.getResources().getStringArray(R.array.cities);
        ages = context.getResources().getIntArray(R.array.ages);
        professions = context.getResources().getStringArray(R.array.professions);
        children = context.getResources().getStringArray(R.array.children);
        smoking = context.getResources().getStringArray(R.array.smoking);

        List<User> users = new ArrayList<>();
        for(int i=0; i<displayNames.length; i++){
            users.add(getUser(i));
        }

        return users;
    }

    private static User getUser(int index){
        User user = new User();
        Random random = new Random(System.currentTimeMillis());
        user.setPictureUrl(avatarUrls[index]);
        user.setUserName(displayNames[index]);
        user.setDateModified(System.currentTimeMillis());
        user.setCity(cities[random.nextInt(6)]);
        user.setSmokingAttitude(smoking[random.nextInt(6)]);
        user.setUserAge(ages[random.nextInt(6)]);
        user.setProfession(professions[random.nextInt(6)]);
        user.setWishOfChildren(children[random.nextInt(6)]);
        return user;
    }
}
