package com.android.greenfoodchallenge.controller.parcelables;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.greenfoodchallenge.model.Pledge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

  /*
   *  parcelable wrapper for Pledge class so can be unit-testable and parcelable
   */

public class PledgeParcelable extends Pledge implements Parcelable {

  private final String ORIGINAL_KEY = "Original Servings";
  private final String PLEDGED_KEY  = "Pledged Servings";
  private final String FOODS_KEY    = "Foods";

  public PledgeParcelable() { }

  private PledgeParcelable(Parcel in) {
    ClassLoader tempClassLoader = getClass().getClassLoader();
    Bundle inBundle = in.readBundle(tempClassLoader);
    if (inBundle != null) {
      mFoods = inBundle.getStringArrayList(FOODS_KEY);
      Serializable serialOriginal = inBundle.getSerializable(ORIGINAL_KEY);
      //noinspection unchecked
      mOriginalServings = (HashMap<String, Integer>) serialOriginal;
      Serializable serialPledged = inBundle.getSerializable(PLEDGED_KEY);
      //noinspection unchecked
      mPledgedServings = (HashMap<String, Integer>) serialPledged;
    }
  }

  public static final Creator<PledgeParcelable> CREATOR = new Creator<PledgeParcelable>() {
    @Override
    public PledgeParcelable createFromParcel(Parcel in) {
      return new PledgeParcelable(in);
    }

    @Override
    public PledgeParcelable[] newArray(int size) {
      return new PledgeParcelable[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    Bundle outBundle = new Bundle();
    outBundle.putSerializable(ORIGINAL_KEY, mOriginalServings);
    outBundle.putSerializable(PLEDGED_KEY, mPledgedServings);
    outBundle.putStringArrayList(FOODS_KEY, (ArrayList<String>) mFoods);
    dest.writeBundle(outBundle);
  }
}
