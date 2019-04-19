package com.android.greenfoodchallenge.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.greenfoodchallenge.R;
import com.android.greenfoodchallenge.controller.parcelables.FoodBankParcelable;
import com.android.greenfoodchallenge.controller.parcelables.PledgeParcelable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

/*
 *  contains logic shared by every activity; each one inherits from this
 */

public abstract class BaseActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{

  protected final String FOOD_BANK_TAG = "Food Bank";
  protected final String PLEDGE_TAG = "Pledge";

  protected FoodBankParcelable mFoodBank;
  protected PledgeParcelable mUserPledge;
  protected FirebaseFirestore mFireDatabase;
  protected android.support.v7.widget.Toolbar mToolbar;
  protected DrawerLayout mDrawer;
  protected ActionBarDrawerToggle mDrawerToggle;
  protected FirebaseStorage mFireStorage;
  protected Class mCurrentActivity;

  @Override
  public void onBackPressed() {
    if (mCurrentActivity != LoginActivity.class
          && mCurrentActivity != SplashActivity.class
          && mDrawer.isDrawerOpen(GravityCompat.START)) {
      mDrawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

    switch (menuItem.getItemId()) {
      case R.id.nav_pledge:
        moveToActivity(this, MainActivity.class);
        break;
      case R.id.nav_meals:
        moveToActivity(this, MealsActivity.class);
        break;
      case R.id.nav_browse:
        moveToActivity(this, BrowseActivity.class);
        break;
      case R.id.nav_quiz:
        moveToActivity(this, QuizActivity.class);
        break;
      case R.id.nav_about:
        moveToActivity(this, AboutActivity.class);
        break;
      case R.id.nav_account:
        moveToActivity(this, AccountActivity.class);
        break;
      case R.id.nav_logout:
        this.logoutUser();
        break;
    }
    mDrawer.closeDrawer(GravityCompat.START);
    return true;
  }

  protected void fetchDataFromIntent() {
    Intent intentToReceive = getIntent();
    mFoodBank     = intentToReceive.getParcelableExtra(FOOD_BANK_TAG);
    mUserPledge   = intentToReceive.getParcelableExtra(PLEDGE_TAG);
    if (mFoodBank == null) {
      Log.d(FOOD_BANK_TAG, "null");
    }
    if (mUserPledge == null) {
      Log.d(PLEDGE_TAG, "null");
    }
  }

  protected void moveToActivity(Context fromContext, Class toActivity) {
    Intent newIntent = new Intent(fromContext, toActivity);
    newIntent.putExtra(FOOD_BANK_TAG, mFoodBank);
    newIntent.putExtra(PLEDGE_TAG, mUserPledge);
    startActivity(newIntent);
    finish();
  }

  protected void startToolbar() {

    this.mToolbar = findViewById(R.id.toolbar_home);
    setSupportActionBar(this.mToolbar);
    String toolbarTitle = getString(R.string.toolbar_title_my_pledge);
    this.mToolbar.setTitle(toolbarTitle);
    this.mToolbar.inflateMenu(R.menu.menu_main);
  }

  protected void startDatabase() {
    mFireDatabase = FirebaseFirestore.getInstance();
  }
  protected void startStorage() {
      this.mFireStorage = FirebaseStorage.getInstance();
  }

  protected void startNavDrawer() {
    mDrawer = findViewById(R.id.drawer_layout);
    mDrawerToggle = new ActionBarDrawerToggle(this,
        mDrawer,
        mToolbar,
        R.string.navigation_drawer_open,
        R.string.navigation_drawer_close);
    mDrawer.addDrawerListener(mDrawerToggle);
    mDrawerToggle.syncState();

    NavigationView navigationView = findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    this.setNavHeader(navigationView);
  }

  private void setNavHeader(NavigationView navigationView) {
    View navHeaderView = navigationView.getHeaderView(0);
    FirebaseAuth fireAuth = FirebaseAuth.getInstance();
    FirebaseUser currentFireUser = fireAuth.getCurrentUser();
    if (currentFireUser != null) {
      this.updateNavHeaderUser(navHeaderView, currentFireUser);
    }
  }

  private void updateNavHeaderUser(View navHeaderView, FirebaseUser currentFireUser) {
    String currentFireUserDisplayName = currentFireUser.getDisplayName();
    TextView navHeaderUserName = navHeaderView.findViewById(R.id.text_nav_name);
    navHeaderUserName.setText(currentFireUserDisplayName);

    String currentFireUserEmail = currentFireUser.getEmail();
    TextView navHeaderUserEmail = navHeaderView.findViewById(R.id.text_nav_email);
    navHeaderUserEmail.setText(currentFireUserEmail);
  }

  protected FirebaseUser getCurrentUser() {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    return firebaseAuth.getCurrentUser();
  }

  private void logoutUser() {
    FirebaseAuth.getInstance().signOut();
    moveToActivity(this, LoginActivity.class);
  }
}