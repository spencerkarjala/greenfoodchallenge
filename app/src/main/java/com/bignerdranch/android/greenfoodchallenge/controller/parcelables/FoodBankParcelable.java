package com.bignerdranch.android.greenfoodchallenge.controller.parcelables;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.android.greenfoodchallenge.model.Food;
import com.bignerdranch.android.greenfoodchallenge.model.FoodBank;

import java.io.Serializable;
import java.util.HashMap;

/*
 *  provides a wrapper for the FoodBank class so it can be placed within intents
 *  and shared between activities
 */

public class FoodBankParcelable extends FoodBank implements Parcelable {

  private final String HASHMAP_BUNDLE_KEY = "HashMap";
  private final String STRINGLIST_BUNDLE_KEY = "StringList";

  public FoodBankParcelable() { }

  private FoodBankParcelable(Parcel in) {
    ClassLoader tempClassLoader = getClass().getClassLoader();
    Bundle inBundle = in.readBundle(tempClassLoader);
    if (inBundle != null) {
      Serializable tempSerializable = inBundle.getSerializable(HASHMAP_BUNDLE_KEY);
      // noinspection unchecked <-- removes cast warning
      mFoodBank = (HashMap<String, Food>) tempSerializable;
      mFoodNameList = inBundle.getStringArrayList(STRINGLIST_BUNDLE_KEY);
    }
  }

  public static final Creator<FoodBank> CREATOR = new Creator<FoodBank>() {
    @Override
    public FoodBank createFromParcel(Parcel in) {
      return new FoodBankParcelable(in);
    }

    @Override
    public FoodBank[] newArray(int size) {
      return new FoodBankParcelable[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  //writes data to a parcel so the object can be transferred between activities
  @Override
  public void writeToParcel(Parcel dest, int flags) {
    Bundle outBundle = new Bundle();
    outBundle.putSerializable(HASHMAP_BUNDLE_KEY, mFoodBank);
    outBundle.putStringArrayList(STRINGLIST_BUNDLE_KEY, mFoodNameList);
    dest.writeBundle(outBundle);
  }
}
