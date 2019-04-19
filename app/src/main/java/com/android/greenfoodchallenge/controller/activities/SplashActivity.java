package com.android.greenfoodchallenge.controller.activities;


import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.android.greenfoodchallenge.R;
import com.android.greenfoodchallenge.controller.parcelables.FoodBankParcelable;
import com.android.greenfoodchallenge.controller.parcelables.PledgeParcelable;

/*
 *  activity that the user sees when opening the app for the first time.
 *  initializes any data structures that will be shared throughout activity states
 */

public class SplashActivity extends BaseActivity {

  private GestureDetectorCompat mGestureDetector;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    mCurrentActivity = SplashActivity.class;

    this.initializeData();
    mGestureDetector = new GestureDetectorCompat(this, new GestureListener());
  }

  @Override
  public boolean onTouchEvent(MotionEvent event){
      mGestureDetector.onTouchEvent(event);
      return super.onTouchEvent(event);
  }

  class GestureListener extends GestureDetector.SimpleOnGestureListener{

      @Override
      public boolean onDown(MotionEvent event){
          return true;
      }

      @Override
      public boolean onFling(MotionEvent event1,
                             MotionEvent event2,
                             float velocityX,
                             float velocityY){

          float deltaY = event2.getY() - event1.getY();
          if(deltaY  < -100){
              moveToMainScreen();
          }
          return true;
      }
  }

  private void moveToMainScreen() {
    moveToActivity(this, LoginActivity.class);
    overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
  }

  private void initializeData() {
    this.initFoodBank();
    this.initPledge();
  }

  private void initFoodBank() {
    mFoodBank = new FoodBankParcelable();
  }

  private void initPledge() {
    mUserPledge = new PledgeParcelable();
  }
}