package com.android.greenfoodchallenge.controller.activities;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.greenfoodchallenge.R;

import java.util.ArrayList;
import java.util.List;

/*
 *  activity that allows users to input their custom serving amounts for
 *  food types to use them in future calculations
 */

public class UserInputActivity extends BaseActivity {

  private int counter = 0;
  private List<String> mFoodTextViews;
  private EditText newEditText;
  final int NUMBER_OF_FOODS = 7;
  private TextView mFoodText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_input);

    mFoodTextViews = new ArrayList<>();

    fetchDataFromIntent();
    this.initResources();
    startToolbar();
    startNavDrawer();
    setTitle(getString(R.string.toolbar_title_calculator));

    setScreen();

    this.keyboardListener();

  }

  private void initResources() {
    //initialize spinner
    Spinner mSpinner = findViewById(R.id.spinner);
    ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(this,
        R.array.spinner_options,
        android.R.layout.simple_spinner_item);
    mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mSpinner.setAdapter(mAdapter);

    //initialize TextView
    mFoodText = findViewById(R.id.input_screen_food_choices);

    //initialize Food types from resources
    this.initFoodText();
  }

  //this function listens and reacts to "Next" actions on keyboard
  private void keyboardListener() {
    newEditText = findViewById(R.id.input_screen_edit_0);
    final Context currentContext = this;
    newEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_NEXT) { //user has hit Next on keyboard
          getUserServings(counter);
          counter++;
          if (counter == NUMBER_OF_FOODS) {
            moveToActivity(currentContext, ResultsActivity.class);
          }
          else {
            setScreen();
          }
          handled = true;
        }
        return handled;
      }
    });
  }

  private void initFoodText() {
    //get working resources and id of food choices array
    Resources inputScreenResources = getResources();
    int foodTextArrayId = R.array.input_screen_food_choices;
    TypedArray foodStringIds = inputScreenResources.obtainTypedArray(foodTextArrayId);

    // load entire typed array into activity local List
    for( int i = 0; i < NUMBER_OF_FOODS; i ++ ) {
      mFoodTextViews.add(foodStringIds.getString(i));
    }

    //recycle memory used for typed array
    foodStringIds.recycle();
  }

  private void setScreen() {
    mFoodText.setText(mFoodTextViews.get(counter));
  }

  //collects user input from each serving field and saves it to a container
  private void getUserServings(int mCounter) {

    String foodName = mFoodTextViews.get(mCounter);

    int newPledgedValue;
    //if user has entered a value in that field, it must be a positive integer; save it
    if (newEditText.length() > 0) {
      Editable userInputContainer = newEditText.getText();
      String userInputString = userInputContainer.toString();
      newPledgedValue = Integer.parseInt(userInputString);
      mUserPledge.setOriginalServing(foodName, newPledgedValue);
      mUserPledge.setPledgedServing(foodName, newPledgedValue);
    }
    //if user hadn't entered a value in that field, save it as 0 servings
    else {
      mUserPledge.setOriginalServing(foodName, 0);
      mUserPledge.setPledgedServing(foodName, 0);
    }
    newEditText.setText("");
  }

}
