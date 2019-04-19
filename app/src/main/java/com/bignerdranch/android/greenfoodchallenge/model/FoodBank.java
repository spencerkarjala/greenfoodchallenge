package com.bignerdranch.android.greenfoodchallenge.model;

import java.util.ArrayList;
import java.util.HashMap;

/*
 *  data structure that stores all default food values for emission calculations
 */

public class FoodBank {
  protected HashMap<String, Food> mFoodBank = new HashMap<>();
  protected ArrayList<String> mFoodNameList = new ArrayList<>();

  protected FoodBank() {
    this.putFood("Beef", 0.075, 27.0);
    this.putFood("Pork", 0.075, 12.1);
    this.putFood("Chicken", 0.075, 6.9);
    this.putFood("Fish", 0.075, 6.1);
    this.putFood("Eggs", 0.100, 4.8);
    this.putFood("Beans", 0.178, 2);
    this.putFood("Vegetables", 0.096, 2);
  }

  private void putFood(String newFoodName, double newFoodKgPerServing, double newFoodCarbonPerKg) {
    Food newFood = new Food(newFoodName, newFoodKgPerServing, newFoodCarbonPerKg);
    mFoodBank.put(newFoodName, newFood);
    mFoodNameList.add(newFoodName);
  }

  public Food getFood(String foodName) {
    checkForNullArgument(foodName);
    Food foodToFind = mFoodBank.get(foodName);

    if (foodToFind == null) {
      return new Food("", 0.0, 0.0);
    }

    return foodToFind;
  }

    double getFoodKgPerServing(String foodName) {
    checkForNullArgument(foodName);
    Food foodToFind = mFoodBank.get(foodName);

    if (foodToFind == null) {
      return -1;
    }

    return foodToFind.getKgPerServing();
  }

  double getFoodCarbonPerServing(String foodName) {
    checkForNullArgument(foodName);
    Food foodToFind = mFoodBank.get(foodName);

    if (foodToFind == null) {
      return -1;
    }

    return foodToFind.getCarbonPerKg();
  }

  public ArrayList<String> getStoredFoods() {
    return mFoodNameList;
  }

  private void checkForNullArgument(Object newObject) {
    if (newObject == null) {
      throw new IllegalArgumentException("FoodBank passed null object");
    }
  }
}