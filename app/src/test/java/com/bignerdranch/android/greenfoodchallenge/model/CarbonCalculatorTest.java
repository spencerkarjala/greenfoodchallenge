package com.bignerdranch.android.greenfoodchallenge.model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CarbonCalculatorTest {

  @org.junit.Test
  public void testCalcCarbonForFood() {
    double testServing = 2;
    Food testFood = new Food("Beans", 4, 8 );
    double expectedValue = testServing * testFood.getCarbonPerKg() * testFood.getKgPerServing();
    assertEquals(CarbonCalculator.calcCarbonForFood(testServing, testFood), expectedValue, 0.0);

    testServing = 0;
    expectedValue = testServing * testFood.getCarbonPerKg() * testFood.getKgPerServing();
    assertEquals(CarbonCalculator.calcCarbonForFood(testServing, testFood), expectedValue, 0.0);

    testServing = -34.1;
    expectedValue = testServing * testFood.getCarbonPerKg() * testFood.getKgPerServing();
    assertEquals(CarbonCalculator.calcCarbonForFood(testServing, testFood), expectedValue, 0.0);
  }

  @org.junit.Test
  public void testConvertWeeklyToAnnually() {
    double WEEKS_PER_YEAR = 52.143;
    double testDouble = 163.2543;
    double testTotal = testDouble * WEEKS_PER_YEAR;
    assertEquals(CarbonCalculator.convertWeeklyToAnnually(testDouble), testTotal,0);
  }

  @org.junit.Test
  public void testConvertEmissionsToSaplings() {
    int testEmissions = 1234;
    double SAPLINGS_PER_CARBON = 2.6 / 100;
    int testSaplings = (int) (testEmissions * SAPLINGS_PER_CARBON);
    assertEquals(CarbonCalculator.convertEmissionsToSaplings(testEmissions), testSaplings);
  }

  @org.junit.Test
  public void testConvertEmissionsToVehicles() {
    int testEmissions = 4321;
    double KM_PER_CARBON = 394 / 100;
    int testVehicles = (int) (testEmissions * KM_PER_CARBON);
    assertEquals(CarbonCalculator.convertEmissionsToVehicles(testEmissions), testVehicles);
  }

  @org.junit.Test
  public void testConvertToVancouverPop() {
    int testEmissions = 15;
    long POPULATION_OF_METRO_VANCOUVER = 2463000;
    long testVancouver = testEmissions * POPULATION_OF_METRO_VANCOUVER / 1000;
    assertEquals(CarbonCalculator.convertToVancouverPop(testEmissions), testVancouver);
  }

  @org.junit.Test
  public void testConvertServingsToCarbon() {
    String testFoodName = "abcd";
    double testFoodKgPerServing = 1234.4321;
    double testFoodCarbonPerKg  = 4321.1234;
    Food testFood = new Food(testFoodName, testFoodKgPerServing, testFoodCarbonPerKg);
    int testServings = 10;
    double testCarbon = testServings * testFoodKgPerServing * testFoodCarbonPerKg;
    assertEquals(CarbonCalculator.convertServingsToCarbon(testServings, testFood), testCarbon, 0);
  }

  @org.junit.Test
  public void testCalcIndividualCarbonSavedPerYear() {
    Pledge testPledge = new Pledge();
    FoodBank testFoodBank = new FoodBank();

    String testFoodName = "Beef";
    Food testFood = testFoodBank.getFood(testFoodName);
    double WEEKS_PER_YEAR = 52.143;
    int testOriginalServing = 1234;
    int testPledgedServing = 4321;

    testPledge.setOriginalServing(testFoodName, testOriginalServing);
    testPledge.setPledgedServing(testFoodName, testPledgedServing);

    double testDifference = testOriginalServing - testPledgedServing;
    double testCarbon = CarbonCalculator.convertServingsToCarbon(testDifference, testFood);

    assertEquals(CarbonCalculator
        .calcIndividualCarbonSavedPerYear(
            testPledge,
            testFoodBank,
            testFoodName),
        testCarbon * WEEKS_PER_YEAR,
        0);
  }

  @org.junit.Test
  public void testCalcChangeInServings() {
    Pledge testPledge = new Pledge();
    String testFoodName = "Chicken";
    assertEquals(CarbonCalculator.calcChangeInServings(testPledge, testFoodName), 0, 0);

    int testOriginalServing = 462;
    int testPledgedServing = 12;
    int testDifference = testOriginalServing - testPledgedServing;

    testPledge.setOriginalServing(testFoodName, testOriginalServing);
    testPledge.setPledgedServing(testFoodName, testPledgedServing);

    assertEquals(CarbonCalculator.calcChangeInServings(testPledge, testFoodName), testDifference, 0);
  }

  @org.junit.Test
  public void testCalcTotalCarbonSavedPerYear() {
    Pledge testPledge = new Pledge();
    FoodBank testFoodBank = new FoodBank();
    List<Integer> testOriginalServings = new ArrayList<>();
    List<Integer> testPledgedServings  = new ArrayList<>();
    List<String> testFoods = testPledge.getStoredFoods();
    int NUMBER_OF_FOODS = 7;
    double testTotalServing = 0;

    for (int i = 0; i < NUMBER_OF_FOODS; i++) {
      testOriginalServings.add(i, (i + 1) * 100);
      testPledgedServings.add(i, (i + 1)*10);
    }

    for (int i = 0; i < NUMBER_OF_FOODS; i++) {
      String currentFoodName = testFoods.get(i);
      Food currentFood = testFoodBank.getFood(currentFoodName);

      int currentOriginalServing = testOriginalServings.get(i);
      int currentPledgedServing  = testPledgedServings.get(i);

      testPledge.setOriginalServing(currentFoodName, currentOriginalServing);
      testPledge.setPledgedServing(currentFoodName, currentPledgedServing);

      double currentServingDiff = currentOriginalServing - currentPledgedServing;
      double currentCarbon = CarbonCalculator
          .convertServingsToCarbon(
              currentServingDiff,
              currentFood);
      testTotalServing += currentCarbon;
    }

    testTotalServing = CarbonCalculator.convertWeeklyToAnnually(testTotalServing);

    assertEquals(CarbonCalculator
        .calcTotalCarbonSavedPerYear(
            testPledge,
            testFoodBank),
        testTotalServing,
        0);
  }
}