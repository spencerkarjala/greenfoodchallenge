package com.bignerdranch.android.greenfoodchallenge.model;

import java.util.List;

/*
 *  model class that provides a static interface for calculating carbon emissions
 */

public abstract class CarbonCalculator {

  @SuppressWarnings("WeakerAccess")
  public static double calcCarbonForFood(double userServings, Food foodToCalc) {

    double kgPerServing = foodToCalc.getKgPerServing();
    double carbonPerKg  = foodToCalc.getCarbonPerKg();

    //noinspection UnnecessaryLocalVariable
    double userCarbonForFood = userServings * kgPerServing * carbonPerKg;
    return userCarbonForFood;
  }

  public static double convertWeeklyToAnnually(double valueToConvert) {
    double WEEKS_PER_YEAR = 52.143;
    return valueToConvert * WEEKS_PER_YEAR;
  }

  public static double calcIndividualCarbonSavedPerYear(Pledge pledge,
                                                  FoodBank foodInfo,
                                                  String foodToCalc) {

    double originalServings = pledge.getOriginalServings(foodToCalc);
    double pledgedServings  = pledge.getPledgedServings(foodToCalc);
    Food foodToGet = foodInfo.getFood(foodToCalc);

    double diffServings = originalServings - pledgedServings;
    double carbonSaved  = convertServingsToCarbon(diffServings, foodToGet);

    return convertWeeklyToAnnually(carbonSaved);
  }

  @SuppressWarnings("WeakerAccess")
  public static double calcChangeInServings(Pledge pledge, String foodToCalc) {
    double originalServings = pledge.getOriginalServings(foodToCalc);
    double pledgedServings  = pledge.getPledgedServings(foodToCalc);
    return originalServings - pledgedServings;
  }

  public static double calcTotalCarbonSavedPerYear(Pledge pledge, FoodBank foodInfo) {

    double totalCarbonSaved = 0.0;
    List<String> storedFoods = pledge.getStoredFoods();
    int numberOfFoods = storedFoods.size();

    for (int i = 0; i < numberOfFoods; i++) {
      String foodNameToCalc   = storedFoods.get(i);
      Food   foodToCalc       = foodInfo.getFood(foodNameToCalc);

      double servingsDifference = calcChangeInServings(pledge, foodNameToCalc);
      double currentCarbonSaved = convertServingsToCarbon(servingsDifference, foodToCalc);
      totalCarbonSaved += currentCarbonSaved;
    }

    return convertWeeklyToAnnually(totalCarbonSaved);
  }

  public static double convertServingsToCarbon(double numberOfServings, Food foodToConvert) {
    double kgPerServing = foodToConvert.getKgPerServing();
    double carbonPerKg  = foodToConvert.getCarbonPerKg();
    return numberOfServings * kgPerServing * carbonPerKg;
  }

  public static int convertEmissionsToSaplings(int differenceInEmissions) {
    //conversion: 2.6 seedlings grown for 10 years per 100kg CO2e
    double SAPLINGS_PER_CARBON = 2.6 / 100;
    return (int) (differenceInEmissions * SAPLINGS_PER_CARBON);
  }

  public static int convertEmissionsToVehicles(int differenceInEmissions) {
    //conversion: 394 kilometers per 100kg of CO2e
    double KM_PER_CARBON = 394 / 100;
    return (int) (differenceInEmissions * KM_PER_CARBON);
  }

  @SuppressWarnings("WeakerAccess")
  public static long convertToVancouverPop(int valueToConvert) {
    long POPULATION_OF_METRO_VANCOUVER = 2463000;
    //divide by 1000 to convert to metric tons
    return (valueToConvert * POPULATION_OF_METRO_VANCOUVER) / 1000;
  }
}