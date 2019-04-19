package com.bignerdranch.android.greenfoodchallenge.controller.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.greenfoodchallenge.R;
import com.bignerdranch.android.greenfoodchallenge.controller.activities.MealsActivity;
import com.bignerdranch.android.greenfoodchallenge.controller.listeners.ItemClickListener;
import com.bignerdranch.android.greenfoodchallenge.controller.parcelables.FoodBankParcelable;
import com.bignerdranch.android.greenfoodchallenge.controller.parcelables.PledgeParcelable;
import com.bignerdranch.android.greenfoodchallenge.model.Meal;
import com.bignerdranch.android.greenfoodchallenge.model.Pledge;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/*
 *  Creates and manages cards and the related view objects
 *  including image rendering and OnClick handlers
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

  private final String TAG = "CardAdapter";

  final static public int FLAG_HORIZONTAL = 0;
  final static public int FLAG_VERTICAL = 1;
  private List<?> mRecyclerData;
  private int mOrientation;
  private Context mCallerContext;
  private FoodBankParcelable mFoodBank;
  private PledgeParcelable mPledgeBank;

  public CardAdapter(List<?> newData,
                     Context callerContext,
                     int orientation,
                     FoodBankParcelable FoodBank,
                     PledgeParcelable PledgeBank) {
    this.mRecyclerData = newData;
    this.mCallerContext = callerContext;
    this.mOrientation = orientation;
    this.mFoodBank = FoodBank;
    this.mPledgeBank = PledgeBank;
  }

  public CardAdapter(List<?> newData, Context callerContext, int orientation) {
    mRecyclerData = newData;
    this.mCallerContext = callerContext;
    this.mOrientation = orientation;
  }

  @NonNull
  @Override
  public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    int layoutId = -1;
    switch (mOrientation) {
      case FLAG_HORIZONTAL:
        layoutId = R.layout.card_horizontal;
        break;
      case FLAG_VERTICAL:
        layoutId = R.layout.card_vertical;
        break;
    }
    View v = LayoutInflater.from(viewGroup.getContext())
            .inflate(layoutId, viewGroup, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull CardAdapter.ViewHolder viewHolder, int i) {
    FirebaseStorage fireStorage = FirebaseStorage.getInstance();
    StorageReference storageRef = fireStorage.getReference();

    if (this.getStoredType(i) == Meal.class) {
      Meal mealToSet = (Meal) mRecyclerData.get(i);
      this.setMealCard(mealToSet, storageRef, viewHolder, i);
    }
    else if (this.getStoredType(i) == Pledge.class) {
      Pledge pledgeToSet = (Pledge) mRecyclerData.get(i);
      this.setPledgeCard(pledgeToSet, storageRef, viewHolder);
    }
  }

  private void setMealCard(final Meal mealToSet,
                           StorageReference storageRef,
                           final ViewHolder viewHolder,
                           int i) {
    String mealPictureId = mealToSet.getMealImg();
    final StorageReference imageRef = storageRef.child("meals/" + mealPictureId);
    Glide.with(mCallerContext)
            .load(imageRef)
            .into(viewHolder.mImageViewCardPic);
    viewHolder.mTextViewCardTitle.setText(mealToSet.getMealName());

    Log.d(TAG, "setItemClickListener");
    final int index = i;
    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "works");
            MealsActivity.displayUserMealOnClick(mealToSet,
                imageRef,
                mRecyclerData,
                index,
                mCallerContext,
                mFoodBank,
                mPledgeBank);
        }
    });
  }

  private void setPledgeCard(Pledge pledgeToSet,
                             StorageReference storageRef,
                             ViewHolder viewHolder) {
    String pledgeUserId = pledgeToSet.getUser();
    String pledgeAmount = String.valueOf((int)pledgeToSet.getTotalPledge()) + " CO2e";
    viewHolder.mTextViewCardTitle.setText(pledgeUserId);
    viewHolder.mPledgeSubText.setText(pledgeAmount);
  }

  private Class getStoredType(int index) {
    try {
      Object firstStoredObject = mRecyclerData.get(index);
      return firstStoredObject.getClass();
    } catch(NullPointerException e) {
      Log.d(TAG, "getStoredType failed: " + e.getMessage());
    }
    return null;
  }

  @Override
  public int getItemCount() {
    return mRecyclerData.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ItemClickListener mItemClickListener;
    TextView mTextViewCardTitle;
    ImageView mImageViewCardPic;
    TextView mPledgeSubText;

    ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.mTextViewCardTitle = itemView.findViewById(R.id.title);
      this.mImageViewCardPic = itemView.findViewById(R.id.imageview_card);
      this.mPledgeSubText = itemView.findViewById(R.id.pledge_amount);
    }

    @Override
    public void onClick(View v) {
      Log.d(TAG, "klik");
      this.mItemClickListener.onClick(v, getAdapterPosition(), false);
    }
  }
}