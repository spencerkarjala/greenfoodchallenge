package com.android.greenfoodchallenge.model;

/*
 *  Models a meal object that describes food items and where they are from
 */

public class Meal {

  private String mealName;
  private String mealProtein;
  private String restaurantName;
  private String restaurantLoc;
  private String mealImg;
  private String mealDesc;
  private String uID;

  public Meal() { }

  public String getMealName() {
    return this.mealName;
  }

  public String getMealProtein() {
    return this.mealProtein;
  }

  public String getRestaurantName() {
    return this.restaurantName;
  }

  public String getRestaurantLoc() {
    return this.restaurantLoc;
  }

  public String getMealImg() {
    return this.mealImg;
  }

  public String getMealDesc() {
    return this.mealDesc;
  }

  public String getuID() {
    return this.uID;
  }

  public void setMealName(String mealName) {
    this.mealName = mealName;
  }

  public void setMealProtein(String mealProtein) {
    this.mealProtein = mealProtein;
  }

  public void setRestaurantName(String restaurantName) {
    this.restaurantName = restaurantName;
  }

  public void setRestaurantLoc(String restaurantLoc) {
    this.restaurantLoc = restaurantLoc;
  }

  public void setMealImg(String mealImg) {
    this.mealImg = mealImg;
  }

  public void setMealDesc(String mealDesc) {
    this.mealDesc = mealDesc;
  }

  public void setuID(String uID) {
    this.uID = uID;
  }
}
