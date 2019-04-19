package com.android.greenfoodchallenge.model;

import java.util.HashMap;
import java.util.Map;

  /*
   *  models user information like user id's, permissions, location
   */

public class User {

  private String uID;
  private int imgID;
  private Boolean mNamePermission;
  private String name;
  private String municipality;
  private double pledgeAmount;

  public User() { }

  @SuppressWarnings("WeakerAccess")
  public User(String uID,
              int imgID,
              Boolean namePermission,
              String name,
              String municipality,
              double pledgeAmount) {
    this.uID = uID;
    this.imgID = imgID;
    this.mNamePermission = namePermission;
    this.name = name;
    this.municipality = municipality;
    this.pledgeAmount = pledgeAmount;
  }

  @SuppressWarnings("WeakerAccess")
  public User(String uID, String name) {
    this.uID = uID;
    this.name = name;
  }

  public String getuID() {
    return this.uID;
  }

  @SuppressWarnings("WeakerAccess")
  public int getImgID() {
    return this.imgID;
  }

  @SuppressWarnings("WeakerAccess")
  public boolean getNamePermission() {
    return mNamePermission;
  }

  public String getName() {
    return this.name;
  }

  @SuppressWarnings("WeakerAccess")
  public String getMunicipality() {
    return this.municipality;
  }

  @SuppressWarnings("WeakerAccess")
  public double getPledgeAmount() {
    return this.pledgeAmount;
  }

  public void setuID(String uID) {
    this.uID = uID;
  }

  public void setImgID(int imgID) {
    this.imgID = imgID;
  }

  public void setNamePermission(boolean namePermission) {
    this.mNamePermission = namePermission;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setMunicipality(String municipality) {
    this.municipality = municipality;
  }

  public void setPledgeAmount(double pledgeAmount) {
    this.pledgeAmount = pledgeAmount;
  }

  @SuppressWarnings("WeakerAccess")
  public Map<String, Object> toMap() {
    HashMap<String, Object> result = new HashMap<>();
    result.put("id_user", uID);
    result.put("id_img", imgID);
    result.put("permission_name", mNamePermission);
    result.put("name", name);
    result.put("municipality", municipality);
    result.put("pledge_amount", pledgeAmount);

    return result;
  }
}