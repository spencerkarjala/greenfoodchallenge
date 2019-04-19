package com.android.greenfoodchallenge.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 *  models any user data in a way that can be pushed to
 *  and pulled from firestore database
 */

public class Pledge {

  private final int NUMBER_OF_FOODS = 7;
  private final String TAG = "Pledge";

  protected List<String> mFoods;
  protected HashMap<String, Integer> mOriginalServings;
  protected HashMap<String, Integer> mPledgedServings;
  private String mUser = "Anonymous";
  private String mLocation = "None";
  private double mTotalPledge;

  public Pledge() {
    this.createStoredFoods();
    this.createOriginalServingsMap();
    this.createPledgedServingsMap();
  }

  private void createStoredFoods() {
    this.mFoods = new ArrayList<>();
    this.mFoods.add("Beef");
    this.mFoods.add("Pork");
    this.mFoods.add("Chicken");
    this.mFoods.add("Fish");
    this.mFoods.add("Eggs");
    this.mFoods.add("Beans");
    this.mFoods.add("Vegetables");
  }

  private void createOriginalServingsMap() {
    this.mOriginalServings = new HashMap<>();

    for (int i = 0; i < NUMBER_OF_FOODS; i++) {
      String foodToPut = mFoods.get(i);
      this.mOriginalServings.put(foodToPut, 0);
    }
  }

  private void createPledgedServingsMap() {
    this.mPledgedServings = new HashMap<>();

    for (int i = 0; i < NUMBER_OF_FOODS; i++) {
      String foodToPut = mFoods.get(i);
      this.mPledgedServings.put(foodToPut, 0);
    }
  }

  public List<String> getStoredFoods() {
    return mFoods;
  }

  public void setOriginalServing(String foodName, int newServingValue) {
    mOriginalServings.put(foodName, newServingValue);
  }

  public void setPledgedServing(String foodName, int newPledgedValue) {
    mPledgedServings.put(foodName, newPledgedValue);
  }

  public double getOriginalServings(String foodToGet) {
    double originalServingsValue = -1.0;
    try {
      //noinspection ConstantConditions
      originalServingsValue = mOriginalServings.get(foodToGet);
    } catch(NullPointerException e) {
      //return -1
    }
    return originalServingsValue;
  }

  public double getPledgedServings(String foodToGet) {
    double pledgedServingsValue = -1.0;
    try {
      //noinspection ConstantConditions
      pledgedServingsValue = mPledgedServings.get(foodToGet);
    } catch(NullPointerException e) {
      //return -1
    }
    return pledgedServingsValue;
  }

  public void setUser(String newUser) {
    this.mUser = newUser;
  }

  public String getUser() {
    return this.mUser;
  }

  public void setLocation(String newLocation) {
    this.mLocation = newLocation;
  }

  public String getLocation() {
    return this.mLocation;
  }

  public void setTotalPledge(double pledgeAmount) {
    this.mTotalPledge = pledgeAmount;
  }

  public double getTotalPledge() {
    return this.mTotalPledge;
  }
}