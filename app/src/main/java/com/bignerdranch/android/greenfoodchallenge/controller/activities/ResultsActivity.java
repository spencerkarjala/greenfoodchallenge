package com.bignerdranch.android.greenfoodchallenge.controller.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bignerdranch.android.greenfoodchallenge.R;
import com.bignerdranch.android.greenfoodchallenge.model.CarbonCalculator;
import com.bignerdranch.android.greenfoodchallenge.model.Pledge;
import com.bignerdranch.android.greenfoodchallenge.model.User;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/*
 *  logic for activity that takes user serving information and give recommendations
 *  for improving their carbon emissions in an easy-to-understand way
 */

public class ResultsActivity extends BaseActivity {

  private final String TAG = "Results Activity";

  private PieChart mPieChart;
  float user_serving_edit;
  private User mNewUser;
  private String municipality;

  private Button mResetButton;
  private Button mPledgeButton;
  private final int whiteColorValue = Color.parseColor("#ffffff");
  private static final int[] CO2e_COLOURS = {
      Color.rgb(120, 10, 10),
      Color.rgb(200, 10, 10),
      Color.rgb(255, 100, 20),
      Color.rgb(50, 50, 150),
      Color.rgb(220, 200, 0),
      Color.rgb(50, 160, 50),
      Color.rgb(30, 120, 30)
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_results);

    startToolbar();
    startNavDrawer();
    setTitle(getString(R.string.toolbar_title_results));
    fetchDataFromIntent();
    this.createPieChart();

    mNewUser = new User();
    mPledgeButton = findViewById(R.id.results_make_a_pledge);

    this.reactToPledgeButton();

    startDatabase();

    this.reactToEntryHighlight();

    this.createButtons();
    this.createResetListener();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    moveToActivity(this, MainActivity.class);
  }

  private void createButtons(){
    this.mPledgeButton = findViewById(R.id.results_make_a_pledge);
    this.mResetButton = findViewById(R.id.buttonReset);
  }

  private void createPieChart() {
    mPieChart = findViewById(R.id.chart_results);
    mPieChart.setHoleColor(Color.TRANSPARENT);
    mPieChart.setBackgroundColor(Color.TRANSPARENT);
    mPieChart.getDescription().setEnabled(false);
    mPieChart.getLegend().setEnabled(false);

    mPieChart.setCenterTextSize(15);
    this.updatePieChart();
  }

  private void updatePieChart() {
    int NUMBER_OF_FOOD_ITEMS = 7;
    List<PieEntry> entries =  new ArrayList<>();
    ArrayList<String> storedFoods = mFoodBank.getStoredFoods();

    mPieChart.clear();
    this.displayPieTotalSavings();

    for (int i = 0; i < NUMBER_OF_FOOD_ITEMS; i++) {
      String foodToUpdate = storedFoods.get(i);
      this.updatePieEntry(foodToUpdate, entries);
    }

    PieDataSet dataSet = new PieDataSet(entries, "CO2e/year");
    dataSet.setValueTextSize((float) 8);
    dataSet.setValueTextColor(whiteColorValue);
    dataSet.setSliceSpace(2);
    dataSet.setColors(CO2e_COLOURS);
    dataSet.setValueFormatter(new MyValueFormatter());

    PieData pieData= new PieData(dataSet);
    mPieChart.setData(pieData);
    mPieChart.setElevation(20);
    mPieChart.invalidate();   // refresh chart
  }

  private void updatePieEntry(String foodToUpdate, List<PieEntry> entries) {
    PieEntry newPieEntry = new PieEntry((int)mUserPledge.getPledgedServings(foodToUpdate), foodToUpdate);
    entries.add(newPieEntry);
  }

  private void resetPieChart() {
    int NUMBER_OF_FOOD_ITEMS = 7;
    List<PieEntry> entries =  new ArrayList<>();
    ArrayList<String> storedFoods = mFoodBank.getStoredFoods();

    mPieChart.clear();
    this.displayPieTotalSavings();

    for (int i = 0; i < NUMBER_OF_FOOD_ITEMS; i++) {
      String foodToUpdate = storedFoods.get(i);
      this.resetPieEntry(foodToUpdate, entries);
      mUserPledge.setPledgedServing(foodToUpdate,
          (int)mUserPledge.getOriginalServings(foodToUpdate));
    }

    PieDataSet dataSet = new PieDataSet(entries, "CO2e/year");
    dataSet.setValueTextSize((float) 8);
    dataSet.setValueTextColor(whiteColorValue);
    dataSet.setSliceSpace(2);
    dataSet.setColors(CO2e_COLOURS);
    PieData pieData= new PieData(dataSet);
    mPieChart.setData(pieData);
    mPieChart.invalidate();
  }
  private void resetPieEntry(String foodToUpdate, List<PieEntry> entries){
    PieEntry newPieEntry = new PieEntry( (int) mUserPledge.getOriginalServings(foodToUpdate),
        foodToUpdate);
    entries.add(newPieEntry);
  }

  private void resetPieTotalSavings(){
    mPieChart.setCenterText("Total Savings:" + "\n" + "0");
  }

  private void displayPieTotalSavings() {

    int totalSavings = (int) CarbonCalculator.calcTotalCarbonSavedPerYear(mUserPledge, mFoodBank);
    String totalSavingsText = String.valueOf(totalSavings);
    mPieChart.setCenterText("Total Savings:" + "\n" + totalSavingsText);
  }

  public class MyValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public MyValueFormatter() {
      mFormat = new DecimalFormat("###,###,##0");
    }

    @Override
    public String getFormattedValue(float value,
                                    Entry entry,
                                    int dataSetIndex,
                                    ViewPortHandler viewPortHandler) {
      if(value > 0) {
        return mFormat.format(value);
      } else {
        return "";
      }
    }
  }

  private void reactToEntryHighlight() {

    mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

      @Override
      public void onValueSelected(Entry e, Highlight h) {
        final PieEntry pe = (PieEntry) e;

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ResultsActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_edit_servings, null);
        TextView mCurrentServing = mView.findViewById(R.id.current_servings);
        TextView mCurrentSingleEmission = mView.findViewById(R.id.current_CO2e);
        final EditText mUserEdit = mView.findViewById(R.id.user_servings_edit);

        mBuilder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

            if( !mUserEdit.getText().toString().isEmpty() ) {
              user_serving_edit = Float.valueOf(mUserEdit.getText().toString());
              mUserPledge.setPledgedServing(pe.getLabel(), (int) user_serving_edit);
              updatePieChart();

              // remove user's highlight, exit alert dialog
              mPieChart.getOnTouchListener().setLastHighlighted(null);
              mPieChart.highlightValue(null);
            }

          }
        });

        mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {

                      mPieChart.getOnTouchListener().setLastHighlighted(null);
                      mPieChart.highlightValue(null);
                  }});


        String mServing = "Your current weekly serving(s) of "
            + pe.getLabel()
            + " are "
            + (int) mUserPledge.getPledgedServings(pe.getLabel());
        mCurrentServing.setText(mServing);

        double mCO2ProductionWeekly = CarbonCalculator.convertServingsToCarbon(mUserPledge.getPledgedServings(pe.getLabel()),
            mFoodBank.getFood(pe.getLabel()));
        int mCO2ProductionYearly = (int) CarbonCalculator.convertWeeklyToAnnually(mCO2ProductionWeekly);

        String mServingInCO2 = "This amount of servings produces "
            + mCO2ProductionYearly
            + "kg/CO2e per year";

        mCurrentSingleEmission.setText(mServingInCO2);
        mBuilder.setView(mView);
        final AlertDialog edit_dialog = mBuilder.create();
        edit_dialog.show();
      }
      @Override
      public void onNothingSelected() {

      }
    });
  }

  private void reactToPledgeButton() {
    mPledgeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startPermissionsAlertDialog();
      }
    });
  }

  private void startPermissionsAlertDialog() {

    final Context parentContext = this;
    AlertDialog.Builder mBuilder = new AlertDialog.Builder(ResultsActivity.this);
    mBuilder.setMessage(R.string.content_dialog_permissions);

    mBuilder.setPositiveButton(R.string.positive_dialog_permissions,
        new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        startPledgeToDatabaseAlert();
        Log.d("ya", "did stuff");
      }
    });

    mBuilder.setNegativeButton(R.string.negative_dialog_permissions,
        new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        moveToActivity(parentContext, MainActivity.class);
        Log.d("ya", "didn't stuff");
      }
    });

    mBuilder.setNeutralButton(R.string.neutral_dialog_permissions,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Log.d("ya", "stuff");
          }
        });

    final AlertDialog permission_dialog = mBuilder.create();
    permission_dialog.show();
  }


  // this function will display an alert dialog that collects user information before sending pledge to DB
  private void startPledgeToDatabaseAlert() {

    final Context parentContext = this;
    AlertDialog.Builder mBuilder = new AlertDialog.Builder(ResultsActivity.this);
    mBuilder.setTitle(R.string.title_dialog_pledge);
    String dialogContent = this.getString(R.string.content_dialog_pledge_1)
        + " "
        + (int) CarbonCalculator.calcTotalCarbonSavedPerYear(mUserPledge, mFoodBank)
        + this.getString(R.string.content_dialog_pledge_2);
    mBuilder.setMessage(dialogContent);

    View mView = getLayoutInflater().inflate(R.layout.dialog_pledge_info, null);
    RadioGroup radioImages = mView.findViewById(R.id.radio_images);
    final CheckBox namePermission = mView.findViewById(R.id.name_permission);
    final Spinner mySpinner = mView.findViewById(R.id.municipality_spinner);
    List<ImageButton> profileIcons = new ArrayList<>();
    mBuilder.setPositiveButton(R.string.positive_dialog_pledge,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

            Log.d("Database Interaction", "User Shared Pledge to Database");

            createUser(namePermission.isChecked());
            moveToActivity(parentContext, MainActivity.class);
            checkIfUserInDatabase();

            mUserPledge.setUser(mNewUser.getName());
            mUserPledge.setTotalPledge(mNewUser.getPledgeAmount());

            addPledgeToDatabase(mUserPledge, mNewUser);
          }
        });
    mBuilder.setNegativeButton(R.string.negative_dialog_pledge,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Log.d("ya", "didn't stuff");
          }
        });

    // watch spinner for municipality choice
    mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
       @Override
       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         municipality = mySpinner.getSelectedItem().toString();
       }
       @Override
       public void onNothingSelected(AdapterView<?> parent) { municipality = "NULL"; }
     });

    mBuilder.setView(mView);
    final AlertDialog permission_dialog = mBuilder.create();
    permission_dialog.show();
  }

  public void getRadioClick(View view) {
    switch (view.getId()) {
      case R.id.radio0:
        Log.d("abcd", String.valueOf(0));
        mNewUser.setImgID(0);
        break;
      case R.id.radio1:
        Log.d("abcd", String.valueOf(1));
        mNewUser.setImgID(1);
        break;
      case R.id.radio2:
        Log.d("abcd", String.valueOf(2));
        mNewUser.setImgID(2);
        break;
      case R.id.radio3:
        Log.d("abcd", String.valueOf(3));
        mNewUser.setImgID(3);
        break;
    }
  }

  private void createUser(boolean checked) {

    FirebaseAuth fireAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = fireAuth.getCurrentUser();


    //noinspection ConstantConditions
    mNewUser.setuID(currentUser.getUid());
    mNewUser.setMunicipality(municipality);

    if (checked) {
      String nameOfUser = currentUser.getDisplayName();
      String[] nameSplit = nameOfUser.split(" ");
      String nameToSet = nameSplit[0] + " " + nameSplit[1].substring(0, 1);
      mNewUser.setName(nameToSet);
      mNewUser.setNamePermission(true);
    }
    else {
      mNewUser.setName("Anonymous");
      mNewUser.setNamePermission(false);
    }

    mNewUser.setPledgeAmount(CarbonCalculator
        .calcTotalCarbonSavedPerYear(mUserPledge, mFoodBank));
  }


  public void createResetListener(){
    mResetButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showResetDataAlert();
      }
    });
  }

  private void showResetDataAlert() {
    android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(this);
    alertBuilder.setTitle(R.string.dialog_title_reset_data);
    alertBuilder.setMessage(R.string.dialog_message_reset_data);
    alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        resetPieChart();
        resetPieTotalSavings();
      }
    });
    alertBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        //close alert
      }
    });
    final android.app.AlertDialog removeDataDialog = alertBuilder.create();
    removeDataDialog.show();
  }

  private void addUserToDatabase(User newUser) {
    mFireDatabase.collection("users")
        .document(newUser.getuID())
        .set(newUser);
  }

  private void addPledgeToDatabase(Pledge pledgeToAdd, User userToAdd) {
    mFireDatabase.collection("pledges")
        .document(userToAdd.getuID())
        .set(pledgeToAdd);
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
            addUserToDatabase(mNewUser);
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


