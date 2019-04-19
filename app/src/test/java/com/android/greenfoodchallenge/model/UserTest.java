package com.android.greenfoodchallenge.model;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserTest {

  @org.junit.Test
  public void getuID() {
    User mUser = new User();
    assertNotNull(mUser);

    String uID = "myID";
    String mName = "Seto_Kaiba";
    mUser.setuID(uID);
    assertEquals(mUser.getuID(), uID);

    User mNewUser = new User(uID, mName);
    assertNotNull(mNewUser);
    String mNewID = "newID";
    mNewUser.setuID(mNewID);
    assertEquals(mNewUser.getuID(), mNewID);
    assertEquals(mNewUser.getName(), mName);
  }

  @org.junit.Test
  public void getImgID() {
    User mUser = new User();
    assertNotNull(mUser);

    int imgID = 352;
    mUser.setImgID(imgID);
    assertEquals(mUser.getImgID(), imgID);
  }

  @org.junit.Test
  public void getPermissions() {

    User mUser = new User();
    assertNotNull(mUser);

    mUser.setNamePermission(false);
    assertFalse(mUser.getNamePermission());
    mUser.setNamePermission(true);
    assertTrue(mUser.getNamePermission());
  }

  @org.junit.Test
  public void getName() {
    User mUser = new User();
    assertNotNull(mUser);

    String mName = "new_User";
    mUser.setName(mName);
    assertEquals(mUser.getName(), mName);
  }

  @org.junit.Test
  public void getMunicipality() {
    User mUser = new User();
    assertNotNull(mUser);

    String mMunicipality = "Burnaby";
    mUser.setMunicipality(mMunicipality);
    assertEquals(mUser.getMunicipality(), mMunicipality);
  }

  @org.junit.Test
  public void getPledgeAmount() {
    User mUser = new User();
    assertNotNull(mUser);

    double mPledgeAmount = 543.34;
    mUser.setPledgeAmount(mPledgeAmount);
    assertEquals(mUser.getPledgeAmount(), mPledgeAmount, 0);

  }

  @org.junit.Test
  public void setuID() {
    User mUser = new User();
    assertNotNull(mUser);

    String uID = "myID";
    mUser.setuID(uID);
    assertEquals(mUser.getuID(), uID);
  }

  @org.junit.Test
  public void setImgID() {
    User mUser = new User();
    assertNotNull(mUser);

    int imgID = 352;
    mUser.setImgID(imgID);
    assertEquals(mUser.getImgID(), imgID);
  }

  @org.junit.Test
  public void setPermissions() {

    User mUser = new User();
    assertNotNull(mUser);

    mUser.setNamePermission(true);
    assertTrue(mUser.getNamePermission());

    mUser.setNamePermission(false);
    assertFalse(mUser.getNamePermission());
  }

  @org.junit.Test
  public void setName() {
    User mUser = new User();
    assertNotNull(mUser);

    String mName = "new_User";
    mUser.setName(mName);
    assertEquals(mUser.getName(), mName);
  }

  @org.junit.Test
  public void setMunicipality() {
    User mUser = new User();
    assertNotNull(mUser);

    String mMunicipality = "Burnaby";
    mUser.setMunicipality(mMunicipality);
    assertEquals(mUser.getMunicipality(), mMunicipality);
  }

  @org.junit.Test
  public void setPledgeAmount() {
    User mUser = new User();
    assertNotNull(mUser);

    double mPledgeAmount = 543.34;
    mUser.setPledgeAmount(mPledgeAmount);
    assertEquals(mUser.getPledgeAmount(), mPledgeAmount, 0);
  }

  @org.junit.Test
  public void toMap() {
    String uID = "New_User";
    int imgID = 5040;
    String mName = "Waldo";
    String mMunicipality = "Vancouver";
    double mPledgeAmount = 123.1231;

    User mUser = new User(uID, imgID, false,
            mName, mMunicipality, mPledgeAmount);
    assertNotNull(mUser);
    mUser.setNamePermission(true);

    HashMap map = (HashMap) mUser.toMap();
    assertNotNull(map);
    assertEquals(map.get("id_user"), mUser.getuID());
    assertEquals(map.get("id_img"), mUser.getImgID());
    assertEquals(map.get("permission_name"), mUser.getNamePermission());
    assertEquals(map.get("name"), mUser.getName());
    assertEquals(map.get("municipality"), mUser.getMunicipality());
    assertEquals(map.get("pledge_amount"), mUser.getPledgeAmount());
  }
}