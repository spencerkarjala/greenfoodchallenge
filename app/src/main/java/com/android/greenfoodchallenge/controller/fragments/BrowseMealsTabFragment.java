package com.android.greenfoodchallenge.controller.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.greenfoodchallenge.R;
import com.android.greenfoodchallenge.controller.activities.BrowseActivity;
import com.android.greenfoodchallenge.controller.adapters.CardAdapter;
import com.android.greenfoodchallenge.model.Meal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/*
 *  Used for browsing and filtering all meals by any user
 *  in the FireStore database.
 */

public class BrowseMealsTabFragment extends Fragment {

  private final String TAG = "BrowseMealsTab";
  private List<Meal> mMealsTotal;
  private List<Meal> mMealsFiltered;
  private View mRootView;
  private int mCurrentFilter;
  private String mFilterLocation;
  private String mFilterProtein;

  @Override
  public void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @android.support.annotation.Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @android.support.annotation.Nullable ViewGroup container,
                           @android.support.annotation.Nullable Bundle savedInstanceState) {

    mRootView = inflater.inflate(R.layout.fragment_meal_browse, container, false);
    this.getFilterData();
    this.retrieveMeals();
    return mRootView;
  }

  private void getFilterData() {
    Bundle inBundle = getArguments();
    try {
      //noinspection ConstantConditions
      mCurrentFilter = inBundle.getInt("Filter");
      mFilterLocation = inBundle.getString("Location");
      mFilterProtein = inBundle.getString("Protein");
      Log.d(TAG, "getFilterData: " + String.valueOf(mCurrentFilter) + " " + mFilterLocation + " " + mFilterProtein);
    } catch (NullPointerException e) {
      Log.d(TAG, "getFilterData failed: " + e.getMessage());
    }
  }

  private void retrieveMeals() {
    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    fireStore.collection("meals")
        .get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
          @Override
          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
            storeMeals(snapshots);
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.d(TAG, "retrieveMeals failed: " + e.getMessage());
          }
        });
  }

  private void storeMeals(List<DocumentSnapshot> storedMeals) {
    mMealsTotal = new ArrayList<>();
    int numberOfMeals = storedMeals.size();
    for (int i = 0; i < numberOfMeals; i++) {
      DocumentSnapshot currentPledgeDoc = storedMeals.get(i);
      Meal newMeal = this.createMealFromDocument(currentPledgeDoc);
      mMealsTotal.add(newMeal);
    }
    this.filterMealList();
    this.startRecyclerView();
  }

  private void startRecyclerView() {
    RecyclerView recyclerView = mRootView.findViewById(R.id.meal_browse_recycler);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
    recyclerView.setLayoutManager(layoutManager);
    RecyclerView.Adapter mAdapter = new CardAdapter(mMealsFiltered, getContext(), CardAdapter.FLAG_VERTICAL);
    recyclerView.setAdapter(mAdapter);
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

  protected void filterMealList() {
    mMealsFiltered = new ArrayList<>();
    if (mCurrentFilter == BrowseActivity.FILTER_NONE) {
      mMealsFiltered = mMealsTotal;
    }
    else if (mCurrentFilter == BrowseActivity.FILTER_LOCATION) {
      mMealsFiltered = this.sortMealsByLocation();
    }
    else if (mCurrentFilter == BrowseActivity.FILTER_PROTEIN) {
      Log.d(TAG, "entered filter");
      mMealsFiltered = this.sortMealsByProtein();
    }
  }

  protected List<Meal> sortMealsByLocation() {
    if (mFilterLocation.equals("All")) {
      return mMealsTotal;
    }
    List<Meal> sortedMeals = new ArrayList<>();
    int numberOfMeals = mMealsTotal.size();
    for (int i = 0; i < numberOfMeals; i++) {
      Meal currentMeal = mMealsTotal.get(i);
      String currentLocation = currentMeal.getRestaurantLoc();
      if (currentLocation.equals(mFilterLocation)) {
        sortedMeals.add(currentMeal);
      }
    }
    return sortedMeals;
  }

  private List<Meal> sortMealsByProtein() {
    if (mFilterProtein.equals("All")) {
      return mMealsTotal;
    }
    List<Meal> sortedMeals = new ArrayList<>();
    int numberOfMeals = mMealsTotal.size();
    for (int i = 0; i < numberOfMeals; i++) {
      Meal currentMeal = mMealsTotal.get(i);
      String currentProtein = currentMeal.getMealProtein();
      Log.d(TAG, currentProtein + ", " + mFilterProtein);
      if (currentProtein.equals(mFilterProtein)) {
        sortedMeals.add(currentMeal);
      }
    }
    return sortedMeals;
  }
}
