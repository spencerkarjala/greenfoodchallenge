package com.android.greenfoodchallenge.model;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MealTest {

    @Test
    public void testConstructor() {

        Meal userMeal = new Meal();

        assertNotNull(userMeal);

        userMeal.setMealName("Pizza");
        userMeal.setMealProtein("Meat");
        userMeal.setRestaurantName("Luigi's");
        userMeal.setRestaurantLoc("Burnaby");
        userMeal.setMealDesc("Hey this is not an adequate description");
        userMeal.setMealImg("1234512345");
        userMeal.setuID("ABC123");

        assertEquals("Pizza", userMeal.getMealName());
        assertNotEquals("Zaa", userMeal.getMealName());

        assertEquals("Meat", userMeal.getMealProtein());
        assertNotEquals("Fish", userMeal.getMealProtein());

        assertEquals("Luigi's", userMeal.getRestaurantName());
        assertNotEquals("Mario", userMeal.getRestaurantName());

        assertEquals("Burnaby", userMeal.getRestaurantLoc());
        assertNotEquals("Vancouver", userMeal.getRestaurantLoc());

        assertEquals("Hey this is not an adequate description",
            userMeal.getMealDesc());
        assertNotEquals("What's up", userMeal.getMealDesc());

        assertEquals("1234512345", userMeal.getMealImg());
        assertNotEquals("ABC123", userMeal.getMealImg());

        assertEquals("ABC123", userMeal.getuID());
        assertNotEquals("1234512345", userMeal.getMealDesc());


    }

    @Test
    public void testSettersAndGetters() {

        Meal myMeal = new Meal();
        assertNotNull(myMeal);

        //mealName
        String mealName = "abcde";
        myMeal.setMealName(mealName);
        assertEquals(mealName, myMeal.getMealName());
        myMeal.setMealName("newMeal");
        assertNotEquals(mealName, myMeal.getMealName());

        //mealProtein
        String mealProtein = "Pork";
        myMeal.setMealProtein("Ham");
        myMeal.setMealProtein(mealProtein);
        assertEquals(mealProtein, myMeal.getMealProtein());
        assertNotEquals("Ham", myMeal.getMealProtein());

        //restaurantName
        String restaurantName = "Stefano";
        myMeal.setRestaurantName("Stephano");
        myMeal.setRestaurantName(restaurantName);
        assertEquals(restaurantName, myMeal.getRestaurantName());
        assertNotEquals("Stephano", myMeal.getRestaurantName());

        //restaurantLoc
        String restaurantLoc = "Delta";
        String newRestaurantLoc = "Surrey";
        myMeal.setRestaurantLoc(restaurantLoc);
        assertNotEquals(newRestaurantLoc, myMeal.getRestaurantLoc());
        myMeal.setRestaurantLoc(newRestaurantLoc);
        assertNotEquals(restaurantLoc, myMeal.getRestaurantLoc());
        assertEquals(newRestaurantLoc, myMeal.getRestaurantLoc());

        //MealImg
        String mealImg = "id.12345";
        myMeal.setMealImg(mealImg);
        myMeal.setMealImg("id.54321");
        assertNotEquals(mealImg, myMeal.getMealImg());
        assertEquals("id.54321", myMeal.getMealImg());

        //mealDesc
        String mealDesc = "This meal is amazing, I recommend it";
        myMeal.setMealDesc("Hey hey what's up");
        assertNotEquals(mealDesc, myMeal.getMealDesc());
        myMeal.setMealDesc(mealDesc);
        assertEquals(mealDesc, myMeal.getMealDesc());

        //uID
        String userID = "joe1997";
        myMeal.setuID("paul865");
        assertNotEquals(userID, myMeal.getuID());
        myMeal.setuID(userID);
        assertNotEquals("paul865", myMeal.getuID());
        assertEquals(userID, myMeal.getuID());



    }
}
