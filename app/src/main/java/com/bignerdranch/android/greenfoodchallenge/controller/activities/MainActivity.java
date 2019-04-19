package com.bignerdranch.android.greenfoodchallenge.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.android.greenfoodchallenge.R;
import com.bignerdranch.android.greenfoodchallenge.model.CarbonCalculator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

/*
 *  logic for activity on the app's main page - works as main hub
 *  for user to move between activities
 */

public class MainActivity extends BaseActivity {

  private boolean pledgeExists;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fetchDataFromIntent();
    startToolbar();
    startNavDrawer();
    setTitle(getString(R.string.toolbar_title_my_pledge));
    this.startProgressBar();
    this.checkIfUserInDatabase();
    this.checkAndSetVancouverTotal();
  }

  private void startProgressBar() {
    this.setElementVisibility(R.id.container_main_my_pledge, View.INVISIBLE);
    this.setElementVisibility(R.id.container_main_no_pledge, View.INVISIBLE);
    this.setElementVisibility(R.id.loading_icon_main, View.VISIBLE);
  }

  private void setElementVisibility(int elementId, int visibility) {
    findViewById(elementId).setVisibility(visibility);
  }

  private void updateUi() {
    if (pledgeExists) {
      this.createShareButton();
      this.createEditButton();
      this.displayTotalSavings();
    } else {
      createMakePledgeButton();
    }
  }

  private void displayTotalSavings() {

    TextView totalSavingsTextView = findViewById(R.id.textview_my_total);
    TextView totalVehicleTextView = findViewById(R.id.textview_total_vehicles);
    TextView totalSaplingTextView = findViewById(R.id.textview_total_saplings);

    int totalSavingsYearly = (int) CarbonCalculator
        .calcTotalCarbonSavedPerYear(mUserPledge, mFoodBank);
    int totalSavingsVehicles = CarbonCalculator.convertEmissionsToVehicles(totalSavingsYearly);
    int totalSavingsSaplings = CarbonCalculator.convertEmissionsToSaplings(totalSavingsYearly);

    totalSavingsTextView.setText(String.valueOf(totalSavingsYearly));
    totalVehicleTextView.setText(String.valueOf(totalSavingsVehicles));
    totalSaplingTextView.setText(String.valueOf(totalSavingsSaplings));
  }

  private void createMakePledgeButton() {
    Button makePledgeButton = findViewById(R.id.button_make_pledge);
    final Context currentContext = this;
    makePledgeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        moveToActivity(currentContext, UserInputActivity.class);
      }
    });
  }

  private void createShareButton() {
    Button shareButton = findViewById(R.id.button_share_pledge);
    shareButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        double savings = CarbonCalculator.calcTotalCarbonSavedPerYear(mUserPledge, mFoodBank);
        String tweetUrl = "https://twitter.com/intent/tweet?text=" +
            "Hey Twitter-verse! With The GreenFoodChallenge I've pledged to save "
            + savings +
            " tons of CO2 from entering the atmosphere this year by only a few small changes to my diet. You should check it out, too! &url=";
        Uri uri = Uri.parse(tweetUrl);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
      }
    });
  }

  private void createEditButton() {
    Button editButton = findViewById(R.id.button_edit_pledge);
    final Context currentContext = this;
    editButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        moveToActivity(currentContext, ResultsActivity.class);
      }
    });
  }

  private void checkIfUserInDatabase() {
    FirebaseFirestore fireDatabase = FirebaseFirestore.getInstance();
    String currentFireUserId = this.fetchUserFirebaseId();
    //noinspection unused
    Task<DocumentSnapshot> currentUserDocSnapshot = fireDatabase.collection("users")
        .document(currentFireUserId)
        .get()
        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
          @Override
          public void onSuccess(DocumentSnapshot documentSnapshot) {
            fetchDataFromFirestore();
          }
        });
  }

  private boolean checkIfPledgeExists() {
    //noinspection RedundantIfStatement
    if (mUserPledge.getOriginalServings("Beef") > 0.0) {
      return true;
    }
    return false;
  }

  private void checkAndSetView() {
    setElementVisibility(R.id.loading_icon_main, View.INVISIBLE);
    if (checkIfPledgeExists()) {
      setElementVisibility(R.id.container_main_no_pledge, View.INVISIBLE);
      setElementVisibility(R.id.container_main_my_pledge, View.VISIBLE);
      pledgeExists = true;
    } else {
      setElementVisibility(R.id.container_main_my_pledge, View.INVISIBLE);
      setElementVisibility(R.id.container_main_no_pledge, View.VISIBLE);
      pledgeExists = false;
    }
  }

  private void checkAndSetVancouverTotal() {
    final TextView vancouverTotal = findViewById(R.id.vancouver_co2_total);

    FirebaseFirestore fireDatabase = FirebaseFirestore.getInstance();
    fireDatabase.collection("totals")
            .document("total_savings")
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                  double totalSavings = documentSnapshot.getDouble("total_savings");
                  String total = "<b><big>" + String.valueOf(((int)totalSavings)) + "</big></b>";
                  vancouverTotal.setText(Html.fromHtml(getString(R.string.total_pledges_message, total)));
                }
              }
            });
  }

  private void createPledgeFromDatabase(DocumentSnapshot documentSnapshot) {

    List<String> storedFoods = mUserPledge.getStoredFoods();
    HashMap<String, Long> fireOriginalServings = (HashMap<String, Long>) documentSnapshot.get("mOriginalServings");
    HashMap<String, Long> firePledgedServings  = (HashMap<String, Long>) documentSnapshot.get("mPledgedServings");
    final int NUMBER_OF_FOODS = 7;

    for (int i = 0; i < NUMBER_OF_FOODS; i++) {
      String foodToGet = storedFoods.get(i);
      mUserPledge.setOriginalServing(foodToGet, safeLongToInt(fireOriginalServings.get(foodToGet)));
      mUserPledge.setPledgedServing(foodToGet, safeLongToInt(firePledgedServings.get(foodToGet)));
    }
  }

  public static int safeLongToInt(long longToConvert) {
    if (longToConvert < Integer.MIN_VALUE || longToConvert > Integer.MAX_VALUE) {
      throw new IllegalArgumentException
          (longToConvert + "cannot be cast to int too big");
    }
    return (int) longToConvert;
  }

  private void fetchDataFromFirestore() {
    FirebaseFirestore fireDatabase = FirebaseFirestore.getInstance();
    String currentFireUserId = this.fetchUserFirebaseId();

    fireDatabase.collection("pledges")
        .document(currentFireUserId)
        .get()
        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
          @Override
          public void onSuccess(DocumentSnapshot documentSnapshot) {
            if (documentSnapshot.exists()) {
              createPledgeFromDatabase(documentSnapshot);
            }
            checkAndSetView();
            updateUi();
          }
        });
  }

  private String fetchUserFirebaseId() {
    FirebaseAuth fireAuth = FirebaseAuth.getInstance();
    FirebaseUser currentFireUser = fireAuth.getCurrentUser();
    //noinspection ConstantConditions
    return currentFireUser.getUid();
  }
}