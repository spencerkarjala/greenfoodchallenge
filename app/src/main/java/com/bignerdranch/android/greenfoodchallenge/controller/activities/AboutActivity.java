package com.bignerdranch.android.greenfoodchallenge.controller.activities;

import android.os.Bundle;

import com.bignerdranch.android.greenfoodchallenge.R;

/*
 *  Displays information about the app and what the goal is for the user.
 */

public class AboutActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about);

    fetchDataFromIntent();
    startToolbar();
    startNavDrawer();
    setTitle(getString(R.string.toolbar_title_about));
  }
}