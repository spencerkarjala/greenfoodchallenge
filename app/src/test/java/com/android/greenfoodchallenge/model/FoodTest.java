package com.android.greenfoodchallenge.model;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertSame;

public class FoodTest {

  @org.junit.Test
  public void testFoodConstructorSuccess() {
    String testString = "Test String";
    double testKgDouble = 3445;
    double testCarbonDouble = 3000;
    Food testFood = new Food(testString, testKgDouble, testCarbonDouble);
    assertNotNull(testFood);
    assertEquals(testFood.getName(), testString);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testFoodConstructorThrowIllegalArgumentException() {
    String testString = null;
    double testKgDouble = 0;
    double testCarbonDouble = 0;
    //noinspection ConstantConditions
    Food testFood = new Food( testString, testKgDouble, testCarbonDouble);
    assertSame(testFood, null);
  }

  @org.junit.Test
  public void testFoodGetterSetterSuccess() {
    String testString = "";
    double testKgDouble = 3445;
    double testCarbonDouble = 3000;

    Food testFood = new Food(testString, testKgDouble, testCarbonDouble);
    assertNotNull(testFood);
    assertEquals(testFood.getName(), testString);
    assertEquals(testFood.getCarbonPerKg(), testCarbonDouble);
    assertEquals(testFood.getKgPerServing(), testKgDouble);

    testFood.setName(testString);
    assertEquals(testFood.getName(), testString);

    testString = "abcd54321";
    testFood.setName(testString);
    assertEquals(testFood.getName(), testString);

    double testDouble = 5000;
    testFood.setKgPerServing(testDouble);
    assertEquals(testFood.getKgPerServing(), testDouble);

    testFood.setCarbonPerKg(testDouble);
    assertEquals(testFood.getCarbonPerKg(), testDouble);

    testDouble = -23;
    testFood.setKgPerServing(testDouble);
    assertEquals(testFood.getKgPerServing(), testDouble);

    testFood.setCarbonPerKg(testDouble);
    assertEquals(testFood.getCarbonPerKg(), testDouble);

  }
}