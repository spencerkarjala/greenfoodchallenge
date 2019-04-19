package com.bignerdranch.android.greenfoodchallenge.controller.listeners;

import android.view.View;

/*
 *  Simple listener for reacting to RecyclerView card clicks
 */

public interface ItemClickListener {

  void onClick(View view, int position, boolean isLongClick);
}
