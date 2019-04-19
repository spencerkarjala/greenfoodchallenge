package com.bignerdranch.android.greenfoodchallenge.controller.fragments;

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

import com.bignerdranch.android.greenfoodchallenge.R;
import com.bignerdranch.android.greenfoodchallenge.controller.activities.BrowseActivity;
import com.bignerdranch.android.greenfoodchallenge.controller.adapters.CardAdapter;
import com.bignerdranch.android.greenfoodchallenge.model.Pledge;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 *  Used for browsing and filtering all pledges made by any user
 *  in the FireStore database.
 */

public class BrowsePledgesTabFragment extends Fragment {

  final String TAG = "BrowsePledgesTab";

  private List<Pledge> mPledgesTotal;
  private List<Pledge> mPledgesFiltered;
  private View mRootView;
  private int mCurrentFilter;
  private String mFilterLocation;

  @Override
  public void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @android.support.annotation.Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @android.support.annotation.Nullable ViewGroup container,
                           @android.support.annotation.Nullable Bundle savedInstanceState) {

    mRootView = inflater.inflate(R.layout.fragment_pledge_browse, container, false);
    this.getFilterData();
    this.retrievePledges();
    return mRootView;
  }

  private void getFilterData() {
    Bundle inBundle = getArguments();
    try {
      //noinspection ConstantConditions
      mCurrentFilter = inBundle.getInt("Filter");
      mFilterLocation = inBundle.getString("Location");
      Log.d(TAG, String.valueOf(mCurrentFilter) + " " + mFilterLocation);
    } catch (NullPointerException e) {
      Log.d(TAG, "getFilterData failed: " + e.getMessage());
    }
  }

  private void startRecyclerView() {
    RecyclerView recyclerView = mRootView.findViewById(R.id.pledge_browse_recycler);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
    recyclerView.setLayoutManager(layoutManager);
    RecyclerView.Adapter mAdapter = new CardAdapter(mPledgesFiltered, getContext(), CardAdapter.FLAG_VERTICAL);
    recyclerView.setAdapter(mAdapter);
  }

  private void retrievePledges() {
    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    fireStore.collection("pledges")
        .get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
          @Override
          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
            storePledges(snapshots);
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.d(TAG, "retrievePledges Failed: " + e.getMessage());
          }
        });
  }

  private void storePledges(List<DocumentSnapshot> storedPledges) {
    mPledgesTotal = new ArrayList<>();
    int numberOfPledges = storedPledges.size();
    for (int i = 0; i < numberOfPledges; i++) {
      DocumentSnapshot currentPledgeDoc = storedPledges.get(i);
      Pledge newPledge = this.createPledgeFromDocument(currentPledgeDoc);
      mPledgesTotal.add(newPledge);
    }
    filterPledgeList();
    this.startRecyclerView();
  }

  private Pledge createPledgeFromDocument(DocumentSnapshot currentPledgeDoc) {
    Pledge newPledge = new Pledge();
    final int NUM_FOODS = 7;
    List<String> storedFoods = newPledge.getStoredFoods();
    HashMap<String, Integer> userServings = (HashMap<String, Integer>) currentPledgeDoc.get("mOriginalServings");
    HashMap<String, Integer> userPledges = (HashMap<String, Integer>) currentPledgeDoc.get("mPledgedServings");

    String newPledgeLocation = currentPledgeDoc.getString("location");
    String newPledgeUser = currentPledgeDoc.getString("user");
    double newPledgeTotal = currentPledgeDoc.getDouble("totalPledge");

    newPledge.setLocation(newPledgeLocation);
    newPledge.setUser(newPledgeUser);
    newPledge.setTotalPledge(newPledgeTotal);

    for (int i = 0; i < NUM_FOODS; i++) {
      String foodToStore = storedFoods.get(i);
      newPledge.setOriginalServing(foodToStore, Integer.valueOf(String.valueOf(userServings.get(foodToStore))));
      newPledge.setPledgedServing(foodToStore, Integer.valueOf(String.valueOf(userPledges.get(foodToStore))));
    }
    return newPledge;
  }

  protected void filterPledgeList() {
    mPledgesFiltered = new ArrayList<>();
    if (mCurrentFilter == BrowseActivity.FILTER_NONE) {
      mPledgesFiltered = mPledgesTotal;
    }
    else if (mCurrentFilter == BrowseActivity.FILTER_LOCATION) {
      mPledgesFiltered = this.sortPledgesByLocation();
    }
    else if (mCurrentFilter == BrowseActivity.FILTER_BIGGEST) {
      mPledgesFiltered = this.sortPledgesBySize();
    }
  }

  private List<Pledge> sortPledgesByLocation() {
    if (mFilterLocation.equals("All")) {
      return mPledgesTotal;
    }
    List<Pledge> sortedPledges = new ArrayList<>();
    int numberOfPledges = mPledgesTotal.size();
    for (int i = 0; i < numberOfPledges; i++) {
      Pledge currentPledge = mPledgesTotal.get(i);
      String currentLocation = currentPledge.getLocation();
      if (currentLocation.equals(mFilterLocation)) {
        sortedPledges.add(currentPledge);
      }
    }
    return sortedPledges;
  }

  private List<Pledge> sortPledgesBySize() {
    int numberOfPledges = mPledgesTotal.size();
    mPledgesFiltered = mPledgesTotal;
    for (int i = 0; i < numberOfPledges; i++) {
      for (int j = i; j < numberOfPledges; j++) {
        Pledge pledgeBase = mPledgesFiltered.get(i);
        Pledge pledgeTop  = mPledgesFiltered.get(j);
        if (pledgeTop.getTotalPledge() > pledgeBase.getTotalPledge()) {
          mPledgesFiltered.set(i, pledgeTop);
          mPledgesFiltered.set(j, pledgeBase);
        }
      }
    }
    return mPledgesFiltered;
  }
}
