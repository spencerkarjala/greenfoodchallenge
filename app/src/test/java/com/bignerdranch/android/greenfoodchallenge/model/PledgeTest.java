package com.bignerdranch.android.greenfoodchallenge.model;

import java.util.List;

import static org.junit.Assert.*;

public class PledgeTest {

  @org.junit.Test
  public void testPledgeCreation() {
    Pledge testPledge = new Pledge();

    assertEquals(testPledge.getOriginalServings("Beef"), 0, 0);
    assertEquals(testPledge.getOriginalServings("Pork"), 0, 0);
    assertEquals(testPledge.getOriginalServings("Chicken"), 0, 0);
    assertEquals(testPledge.getOriginalServings("Fish"), 0, 0);
    assertEquals(testPledge.getOriginalServings("Beans"), 0, 0);
    assertEquals(testPledge.getOriginalServings("Eggs"), 0, 0);
    assertEquals(testPledge.getOriginalServings("Vegetables"), 0, 0);

    assertEquals(testPledge.getPledgedServings("Beef"), 0, 0);
    assertEquals(testPledge.getPledgedServings("Pork"), 0, 0);
    assertEquals(testPledge.getPledgedServings("Chicken"), 0, 0);
    assertEquals(testPledge.getPledgedServings("Fish"), 0, 0);
    assertEquals(testPledge.getPledgedServings("Beans"), 0, 0);
    assertEquals(testPledge.getPledgedServings("Eggs"), 0, 0);
    assertEquals(testPledge.getPledgedServings("Vegetables"), 0, 0);

    List<String> testFoods = testPledge.getStoredFoods();

    assertEquals(testFoods.get(0), "Beef");
    assertEquals(testFoods.get(1), "Pork");
    assertEquals(testFoods.get(2), "Chicken");
    assertEquals(testFoods.get(3), "Fish");
    assertEquals(testFoods.get(4), "Eggs");
    assertEquals(testFoods.get(5), "Beans");
    assertEquals(testFoods.get(6), "Vegetables");
  }

  @org.junit.Test
  public void testPledgeSetServings() {
    Pledge testPledge = new Pledge();

    testPledge.setOriginalServing("Beef", 15);
    testPledge.setOriginalServing("Chicken", 7);
    testPledge.setOriginalServing("Vegetables", 40);

    testPledge.setPledgedServing("Pork", 3);
    testPledge.setPledgedServing("Fish", 6235);
    testPledge.setPledgedServing("Eggs", 111);


    assertEquals(testPledge.getOriginalServings("Beef"), 15, 0);
    assertEquals(testPledge.getOriginalServings("Chicken"), 7, 0);
    assertEquals(testPledge.getOriginalServings("Vegetables"), 40, 0);

    assertEquals(testPledge.getPledgedServings("Pork"), 3, 0);
    assertEquals(testPledge.getPledgedServings("Fish"), 6235, 0);
    assertEquals(testPledge.getPledgedServings("Eggs"), 111, 0);


  }

  @org.junit.Test
  public void testPledgeGetServingsNull() {
    Pledge testPledge = new Pledge();

    assertEquals(testPledge.getOriginalServings("NOTBEEF"), -1.0, 0);
    assertEquals(testPledge.getPledgedServings("ALSONOTBEEF"), -1.0, 0);
  }

  @org.junit.Test
  public void testSettersAndGetters() {
    Pledge testPledge = new Pledge();

    //User
    testPledge.setUser("Chris Martin");
    assertEquals("Chris Martin", testPledge.getUser());
    String usersName = testPledge.getUser();
    assertEquals(usersName, testPledge.getUser());
    String newUser = "Mikey";
    testPledge.setUser(newUser);
    assertEquals("Mikey", testPledge.getUser());

    //Location
    testPledge.setLocation("Hawaii");
    assertEquals("Hawaii", testPledge.getLocation());
    String hotSpot = testPledge.getLocation();
    assertEquals(hotSpot, testPledge.getLocation());
    hotSpot = "DisneyLand";
    assertNotEquals(hotSpot, testPledge.getLocation());
    testPledge.setLocation(hotSpot);
    assertEquals("DisneyLand", testPledge.getLocation());


    //TotalPledge
    double totalPledge = 54312.124;
    testPledge.setTotalPledge(totalPledge);
    assertEquals(totalPledge, testPledge.getTotalPledge(), 0);
    double newTotalPledge = testPledge.getTotalPledge();
    assertEquals(newTotalPledge, testPledge.getTotalPledge(), 0);
    newTotalPledge = 43.1;
    testPledge.setTotalPledge(newTotalPledge);
    assertNotEquals(totalPledge, testPledge.getTotalPledge());

  }

}