/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rukiasoft.wildcardsproject.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import java.lang.reflect.Field;

import icepick.Icepick;


/**
 * Base activity for activities that need to show a Refresh Layout and a Custom Toolbar
 */
public abstract class ToolbarActivity extends AppCompatActivity {


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    protected void setDefaultValuesForOptions(int id){
        PreferenceManager.setDefaultValues(this, id, false);
    }

    public void setToolbar(Toolbar toolbar){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            try {
                if (toolbar.getClass() != null) {
                    Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
                    f.setAccessible(true);
                    TextView titleTextView = (TextView) f.get(toolbar);
                    titleTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    titleTextView.setFocusable(true);
                    titleTextView.setFocusableInTouchMode(true);
                    titleTextView.requestFocus();
                    titleTextView.setSingleLine(true);
                    titleTextView.setSelected(true);
                    titleTextView.setMarqueeRepeatLimit(-1);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }



}
