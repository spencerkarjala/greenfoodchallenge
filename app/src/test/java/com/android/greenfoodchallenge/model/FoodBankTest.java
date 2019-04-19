package com.android.greenfoodchallenge.model;

import java.util.List;

import static org.junit.Assert.*;

public class FoodBankTest {

  @org.junit.Test
  public void testFoodBankConstructorSuccess() {
    FoodBank testFoodBank = new FoodBank();

    String testStringBeef = "Beef";
    String testStringPork = "Pork";
    String testStringChicken = "Chicken";
    String testStringFish = "Fish";
    String testStringEggs = "Eggs";
    String testStringBeans = "Beans";
    String testStringVegetables = "Vegetables";

    Food testFoodBeef       = testFoodBank.getFood(testStringBeef);
    Food testFoodPork       = testFoodBank.getFood(testStringPork);
    Food testFoodChicken    = testFoodBank.getFood(testStringChicken);
    Food testFoodFish       = testFoodBank.getFood(testStringFish);
    Food testFoodEggs       = testFoodBank.getFood(testStringEggs);
    Food testFoodBeans      = testFoodBank.getFood(testStringBeans);
    Food testFoodVegetables = testFoodBank.getFood(testStringVegetables);

    assertEquals(testFoodBeef.getName(), testStringBeef);
    assertEquals(testFoodPork.getName(), testStringPork);
    assertEquals(testFoodChicken.getName(), testStringChicken);
    assertEquals(testFoodFish.getName(), testStringFish);
    assertEquals(testFoodEggs.getName(), testStringEggs);
    assertEquals(testFoodBeans.getName(), testStringBeans);
    assertEquals(testFoodVegetables.getName(), testStringVegetables);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testFoodBankGetFoodFailure() {

    FoodBank testFoodBank = new FoodBank();
    testFoodBank.getFood(null);
  }

  @org.junit.Test
  public void testGetFoodStatsSuccess() {

    FoodBank testFoodBank = new FoodBank();
    String testFoodName = "Beef";
    double testFoodKgPerServing = 0.075;
    double testFoodCarbonPerKg  = 27.0;

    assertEquals(testFoodBank.getFoodKgPerServing(testFoodName), testFoodKgPerServing, 0.0);
    assertEquals(testFoodBank.getFoodCarbonPerServing(testFoodName), testFoodCarbonPerKg, 0.0);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetFoodKgFailures() {

    FoodBank testFoodBank = new FoodBank();

    String testFoodName = "this food is not entered";
    assertEquals(testFoodBank.getFoodKgPerServing(testFoodName), -1, 0.01);
    testFoodBank.getFoodKgPerServing(null);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetFoodCarbonFailures() {

    FoodBank testFoodBank = new FoodBank();
    String testFoodName = "this food is not entered";
    assertEquals(testFoodBank.getFoodCarbonPerServing(testFoodName), -1, 0.01);
    testFoodBank.getFoodCarbonPerServing(null);
  }

  @org.junit.Test
  public void testFoodBankGetterSetterFailure() {

    FoodBank testFoodBank = new FoodBank();
    Food testEmptyFood = testFoodBank.getFood("food1");

    assertEquals(testEmptyFood.getName(), "");
    assertEquals(testEmptyFood.getCarbonPerKg(), 0.0, 0.01);
    assertEquals(testEmptyFood.getKgPerServing(), 0.0, 0.01);

    Food testFood = new Food("food3", 2.0, 2.0);
    Food testFailureReturnFood = testFoodBank.getFood(testFood.getName());
    assertEquals(testFailureReturnFood.getName(), "");
    assertEquals(testFailureReturnFood.getKgPerServing(), 0.0, 0.0);
    assertEquals(testFailureReturnFood.getCarbonPerKg(), 0.0, 0.0);
  }

  @org.junit.Test
  public void testFoodBankGetStoredFoods() {
    FoodBank testFoodBank = new FoodBank();
    List<String> testStoredFoods = testFoodBank.getStoredFoods();
    assertEquals(testStoredFoods.get(0), "Beef");
    assertEquals(testStoredFoods.get(1), "Pork");
    assertEquals(testStoredFoods.get(2), "Chicken");
    assertEquals(testStoredFoods.get(3), "Fish");
    assertEquals(testStoredFoods.get(4), "Eggs");
    assertEquals(testStoredFoods.get(5), "Beans");
    assertEquals(testStoredFoods.get(6), "Vegetables");
  }
}