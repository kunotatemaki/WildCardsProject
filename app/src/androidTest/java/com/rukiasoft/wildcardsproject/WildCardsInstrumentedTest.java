package com.rukiasoft.wildcardsproject;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.view.View;

import com.rukiasoft.wildcardsproject.activities.StackedViewsActivity;
import com.rukiasoft.wildcardsproject.models.User;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.List;

import static android.support.test.runner.lifecycle.Stage.RESUMED;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class WildCardsInstrumentedTest {


    @Rule
    public ActivityTestRule<StackedViewsActivity> mActivityRule =
            new ActivityTestRule<>(StackedViewsActivity.class);

    @Test
    public void TestSwipeToFlip(){
        //swipe to right
        ViewAction action = new GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER,
                GeneralLocation.CENTER_RIGHT, Press.FINGER);
        Espresso.onView(ViewMatchers.withId(R.id.wildcardstack)).perform(action);
        final StackedViewsActivity activity = mActivityRule.getActivity();
        assertTrue(activity.getFrontCardVisibility() == View.INVISIBLE);
    }

    @Test
    public void TestSwipeButtonToDismiss(){
        //swipe to left
        final StackedViewsActivity activity = mActivityRule.getActivity();
        String[] texts = activity.getCardTexts();
                ViewAction action = new GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER,
                        GeneralLocation.CENTER_LEFT, Press.FINGER);
                Espresso.onView(ViewMatchers.withId(R.id.wildcardstack)).perform(action);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String[] newTexts = activity.getCardTexts();
        assertFalse(texts[0].equals(newTexts[0]));



    }

    @Test
    public void TestAddingCard() {
        // Type text and then press the button.
        final User user = new User();
        user.setUserName("marieta");
        user.setDateModified(System.currentTimeMillis());
        user.setCity("Villabrágima");
        user.setSmokingAttitude("muchísimo");
        user.setUserAge(27);
        user.setProfession("Cuidadora de cuquis");
        user.setWishOfChildren("Si me los cuidan...");

        final StackedViewsActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.resetUsers(user);
                String[] texts = activity.getCardTexts();
                assertTrue(user.getUserName().equals(texts[0]));
                assertTrue(user.getCity().equals(texts[1]));
                assertTrue(user.getProfession().equals(texts[2]));
                assertTrue(String.valueOf(user.getUserAge()).equals(texts[3]));
                assertTrue(user.getSmokingAttitude().equals(texts[4]));
                assertTrue(user.getWishOfChildren().equals(texts[5]));
            }
        });


    }


}
