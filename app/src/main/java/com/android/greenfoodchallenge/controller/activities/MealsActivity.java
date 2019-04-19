package com.android.greenfoodchallenge.controller.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.greenfoodchallenge.R;
import com.android.greenfoodchallenge.controller.adapters.CardAdapter;
import com.android.greenfoodchallenge.controller.parcelables.FoodBankParcelable;
import com.android.greenfoodchallenge.controller.parcelables.PledgeParcelable;
import com.android.greenfoodchallenge.model.Meal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/*
 *  Allows meal creation, deletion, adding pictures, and
 *  scanning through and managing personal user meals.
 */

public class MealsActivity extends BaseActivity {

  private final String TAG = "MealsActivity";
  private static final int REQUEST_IMAGE_CAPTURE = 1;
  private static final int REQUEST_IMAGE_GALLERY = 2;
  private final static String FOOD_BANK_TAG = "Food Bank";
  private final static String PLEDGE_TAG = "Pledge";
  private String mRestaurantLocation = "";
  private String mMealProtein;

  private EditText mEditTextName;
  private EditText mEditRestaurantName;
  private EditText mEditMealDescription;

  private static Button mDeleteMeal;

  private Uri mImageResource = null;
  private static View mView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_meals);

    mView = findViewById(android.R.id.content);

    startDatabase();
    startStorage();
    fetchDataFromIntent();
    startToolbar();
    startNavDrawer();
    setTitle(getString(R.string.toolbar_title_meals));

    this.retrieveMealData();
    this.startButtonDeleteMeal();
    this.startButtonAddMeal();

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
      if (resultData != null) {
        //is returning null
        mImageResource = resultData.getData();
      }
    }
    else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
      if (resultData != null) {
        mImageResource = resultData.getData();
      }
    }
  }


  private void createMeal() {
    String mealName           = this.getEditTextString(mEditTextName);
    String mealProtein        = this.mMealProtein;
    String restaurantName     = this.getEditTextString(mEditRestaurantName);
    String restaurantLocation = this.mRestaurantLocation;
    String mealDescription    = this.getEditTextString(mEditMealDescription);
    int imageId               = new Random().nextInt(2147483647);
    String imgId = String.valueOf(imageId);

    if (mImageResource != null) {
      Log.d(TAG, "what what");
      imgId = mImageResource.getLastPathSegment();
      this.uploadPhotoToStorage(imgId);
    }

    Meal newMeal = new Meal();

    newMeal.setuID(getCurrentUser().getUid());
    newMeal.setMealName(mealName);
    newMeal.setMealProtein(mealProtein);
    newMeal.setRestaurantName(restaurantName);
    newMeal.setRestaurantLoc(restaurantLocation);
    newMeal.setMealDesc(mealDescription);
    newMeal.setMealImg(imgId);

    mFireDatabase.collection("meals")
            .document()
            .set(newMeal)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                moveToActivity(getBaseContext(), MealsActivity.class);
              }
            });
  }

  private Meal createMealFromDocument(DocumentSnapshot currentMealDoc) {
    Meal newMeal = new Meal();
    newMeal.setMealName(currentMealDoc.getString("mealName"));
    newMeal.setMealDesc(currentMealDoc.getString("mealDesc"));
    newMeal.setMealImg(currentMealDoc.getString("mealImg"));
    newMeal.setMealProtein(currentMealDoc.getString("mealProtein"));
    newMeal.setRestaurantLoc(currentMealDoc.getString("restaurantLoc"));
    newMeal.setRestaurantName(currentMealDoc.getString("restaurantName"));
    newMeal.setuID(currentMealDoc.getString("uID"));
    return newMeal;
  }

  private void startButtonAddMeal() {
    Button buttonAddMeal = findViewById(R.id.button_add_meal);
    buttonAddMeal.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startMealToDatabaseAlert();
      }
    });
  }
  private void startButtonDeleteMeal() {
      mDeleteMeal = findViewById(R.id.delete_meal);
      mDeleteMeal.setVisibility(View.INVISIBLE);
  }

  private void startRecyclerView(List<DocumentSnapshot> mealDocuments) {
    List<Meal> userMealList = new ArrayList<>();
    this.getUserMeals(userMealList, mealDocuments);
    startMyMealsRecycler(userMealList);
  }

  private void startMyMealsRecycler(List<Meal> userMealList) {
    RecyclerView mRecyclerView = findViewById(R.id.my_meal_recycler);
    mRecyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    mRecyclerView.setLayoutManager(mLayoutManager);
    RecyclerView.Adapter mAdapter = new CardAdapter(userMealList, this, CardAdapter.FLAG_HORIZONTAL, mFoodBank, mUserPledge);
    mRecyclerView.setAdapter(mAdapter);
  }

  // this function will display an alert dialog that collects user information before sending pledge to DB
  private void startMealToDatabaseAlert() {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
    TextView dialogTitle = new TextView(this);
    int blackValue = Color.parseColor("#000000");
    dialogTitle.setText(R.string.title_dialog_meal);
    dialogTitle.setGravity(Gravity.CENTER_HORIZONTAL);
    dialogTitle.setPadding(0, 30, 0, 0);
    dialogTitle.setTextSize(25);
    dialogTitle.setTextColor(blackValue);
    dialogBuilder.setCustomTitle(dialogTitle);
    View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_meal, null);

    this.startEditTexts(dialogView);
    this.startLocationSpinner(dialogView);
    this.startAddPhotoButton(dialogView);
    this.startMealDialogButtonListeners(dialogBuilder);

    dialogBuilder.setView(dialogView);
    AlertDialog permission_dialog = dialogBuilder.create();
    permission_dialog.show();
  }

  private void startEditTexts(View mView) {
    mEditTextName = mView.findViewById(R.id.meal_name);
    mEditRestaurantName = mView.findViewById(R.id.restaurant_name);
    mEditMealDescription = mView.findViewById(R.id.meal_description);
  }
  private void startLocationSpinner(View mView) {

    final Spinner mySpinnerLocation = mView.findViewById(R.id.location_spinner);
    final Spinner mySpinnerProtein = mView.findViewById(R.id.meal_protein_spinner);

    // watch spinner for municipality choice
      mySpinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mRestaurantLocation = mySpinnerLocation.getSelectedItem().toString();
      }
      @Override
      public void onNothingSelected(AdapterView<?> parent) { mRestaurantLocation = "NULL"; }
    });

      mySpinnerProtein.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              mMealProtein = mySpinnerProtein.getSelectedItem().toString();
          }
          @Override
          public void onNothingSelected(AdapterView<?> parent) { mMealProtein = "NULL"; }
      });

  }

  private void startAddPhotoButton(View dialogView) {
    Button buttonAddExistingPhoto = dialogView.findViewById(R.id.add_existing_photo);

    buttonAddExistingPhoto.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        takeExistingPhoto();
      }
    });
  }

  private void startMealDialogButtonListeners(AlertDialog.Builder dialogBuilder) {
    dialogBuilder.setPositiveButton(R.string.positive_dialog_meal,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                createMeal();
              }
            });
    dialogBuilder.setNeutralButton(R.string.negative_dialog_pledge,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {

              }
            });
  }

  private void getUserMeals(List<Meal> userMealList, List<DocumentSnapshot> mealDocuments) {
    final int numberOfDocuments = mealDocuments.size();
    String currentFireUserId = getCurrentUser().getUid();

    for (int i = 0; i < numberOfDocuments; i++) {
      DocumentSnapshot currentMealDoc = mealDocuments.get(i);
      String currentMealUid = currentMealDoc.getString("uID");
      if (currentFireUserId.equals(currentMealUid)) {
        Meal userMeal = this.createMealFromDocument(currentMealDoc);
        userMealList.add(userMeal);
      }
    }
  }

  private String getEditTextString(EditText editTextToParse) {
    String stringToGet = editTextToParse.getText().toString();
    if (!stringToGet.isEmpty()) {
      return stringToGet;
    }
    return "";
  }

  private void retrieveMealData() {
    mFireDatabase.collection("meals")
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
              @Override
              public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                startRecyclerView(snapshots);
                mDeleteMeal.setVisibility(View.INVISIBLE);
              }
            });
  }


  private void takeExistingPhoto() {
    Intent takePictureIntent = new Intent(Intent.ACTION_PICK);
    takePictureIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
      startActivityForResult(takePictureIntent, REQUEST_IMAGE_GALLERY);
    }
  }

  private void uploadPhotoToStorage(String imageId) {
    StorageReference storageRef = mFireStorage.getReference();
    StorageReference photoRef = storageRef.child("meals/" + imageId);
    photoRef.putFile(mImageResource)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
              @Override
              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "uploadPhotoToStorage succeeded");
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "uploadPhotoToStorage failed: " + e.getMessage());
              }
            });
  }

  public static void displayUserMealOnClick(Meal meal,
                                            final StorageReference imageRef,
                                            final List<?> mRecyclerData,
                                            int i,
                                            final Context callerContext,
                                            final FoodBankParcelable FoodBank,
                                            final PledgeParcelable PledgeBank) {

      mDeleteMeal.setVisibility(View.VISIBLE);

      String setMealInfo;
      TextView mealName = mView.findViewById(R.id.meal_name_text);
      TextView mealProtein = mView.findViewById(R.id.meal_protein_text);
      TextView restaurantName = mView.findViewById(R.id.restaurant_name_text);
      TextView restaurantLocation = mView.findViewById(R.id.restaurant_location_text);
      TextView mealDescription = mView.findViewById(R.id.meal_description_text);
      setMealInfo = "Meal: " + meal.getMealName();
      mealName.setText(setMealInfo);

      setMealInfo = "Main protein: " + meal.getMealProtein();
      mealProtein.setText(setMealInfo);

      setMealInfo = "Restaurant name: " + meal.getRestaurantName();
      restaurantName.setText(setMealInfo);

      setMealInfo = "Restaurant location: " + meal.getRestaurantLoc();
      restaurantLocation.setText(setMealInfo);

      setMealInfo = "Description: " + meal.getMealDesc();
      mealDescription.setText(setMealInfo);


      final int index = i;
      final Meal newMeal = meal;


      mDeleteMeal.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          final FirebaseFirestore dataFireStore = FirebaseFirestore.getInstance();
          dataFireStore.collection("meals")
                  .get()
                  .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                      List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                      int numberOfDocs = snapshots.size();
                      for (int i = 0; i < numberOfDocs; i ++) {
                        String mealId = snapshots.get(i).getString("mealImg");
                        if (Objects.equals(mealId, newMeal.getMealImg())) {
                          String mealToDelete = snapshots.get(i).getId();
                          deleteMeal(mealToDelete, dataFireStore, callerContext, FoodBank, PledgeBank);
                          imageRef.delete();
                          mRecyclerData.remove(index);
                        }
                      }
                    }
                  });
        }
      });
  }

  public static void deleteMeal (String mealId,
                                 FirebaseFirestore dataFireStore,
                                 final Context callerContext,
                                 final FoodBankParcelable FoodBank,
                                 final PledgeParcelable PledgeBank) {

    Log.d("ID", mealId);
    dataFireStore.collection("meals")
            .document(mealId)
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                Log.d("Deletion", "Hooray");
                Intent refresh = new Intent(callerContext, MealsActivity.class);
                refresh.putExtra(FOOD_BANK_TAG, FoodBank);
                refresh.putExtra(PLEDGE_TAG, PledgeBank);
                callerContext.startActivity(refresh);
              }
            });
  }
}