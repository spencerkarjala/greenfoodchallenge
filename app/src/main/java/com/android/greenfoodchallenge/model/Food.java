package com.android.greenfoodchallenge.model;

import java.io.Serializable;

/*
 *  model class used to store information related to the carbon emissions of foods
 */

public class Food implements Serializable {

  private String mName;
  private double mKgPerServing;
  private double mCarbonPerKg;

  Food(String newName, double newKgPerServing, double newCarbonPerKg) {
    this.setName(newName);
    this.setCarbonPerKg(newCarbonPerKg);
    this.setKgPerServing(newKgPerServing);
  }

  String getName() {
    return this.mName;
  }

  void setName(String newName) {
    if (newName == null) {
      throw new IllegalArgumentException("New name cannot be null.");
    }
    this.mName = newName;
  }

  double getKgPerServing() {
    return this.mKgPerServing;
  }

  void setKgPerServing(double newKgPerServing) {
    this.mKgPerServing = newKgPerServing;
  }

  double getCarbonPerKg() {
    return this.mCarbonPerKg;
  }

  void setCarbonPerKg(double newCarbonPerKg) {
    this.mCarbonPerKg = newCarbonPerKg;
  }
}