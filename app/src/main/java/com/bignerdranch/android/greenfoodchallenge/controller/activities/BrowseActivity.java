package com.bignerdranch.android.greenfoodchallenge.controller.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TabHost;

import com.bignerdranch.android.greenfoodchallenge.R;
import com.bignerdranch.android.greenfoodchallenge.controller.fragments.BrowseMealsTabFragment;
import com.bignerdranch.android.greenfoodchallenge.controller.fragments.BrowsePledgesTabFragment;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

public class BrowseActivity extends BaseActivity {

  public static final int FILTER_NONE     = 0;
  public static final int FILTER_LOCATION = 1;
  public static final int FILTER_BIGGEST  = 2;
  public static final int FILTER_PROTEIN  = 3;
  public static final int FILTER_POPULAR  = 4;

  private final int TAB_PLEDGES = 0;
  private String mLocationSort = "All";
  private String mProteinSort = "All";
  private TabHost mTabHost;
  private FragmentManager mFragmentManager;
  private SpeedDialView mSpeedDialView;
  private int mCurrentFilter = 0;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browse);

    fetchDataFromIntent();
    startToolbar();
    startNavDrawer();
    setTitle(getString(R.string.toolbar_title_browse));

    displayTabs();
    setTabListener();
    displayFragment();

    mSpeedDialView = findViewById(R.id.speedDial);
    createFAB();
  }

  // function will customize the speed dial options of FAB based on selected browse tab
  private void createFAB() {

    mSpeedDialView.clearActionItems();
    mSpeedDialView.setMainFabOpenedBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    mSpeedDialView.setMainFabClosedBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    setFABListener();
    int currentTab = mTabHost.getCurrentTab();
    //noinspection UnnecessaryLocalVariable
    if (currentTab == TAB_PLEDGES) {
      createPledgeActionButtons();
    }
    else { createMealsActionButtons(); }
  }

  // function will listen for which speed dial action buttons are selected from the FAB
  private void setFABListener() {
    mSpeedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
      @Override
      public boolean onActionSelected(SpeedDialActionItem actionItem) {

        switch (actionItem.getId()) {
          case R.id.fab_meal_location:
            buildLocationSortDialog();
            break;
          case R.id.fab_protein:
            buildProteinSortDialog();
            break;
          case R.id.fab_popular:
            mCurrentFilter = FILTER_POPULAR;
            changeTabs();
            break;
          case R.id.fab_pledge_location:
            buildLocationSortDialog();
            changeTabs();
            break;
          case R.id.fab_biggest:
            mCurrentFilter = FILTER_BIGGEST;
            changeTabs();
            break;
          default:
            mCurrentFilter = FILTER_NONE;
            changeTabs();
            break;
        }
        return false;
      }
    });
  }

  // function will build alert dialog for user to choose their location sort
  private void buildLocationSortDialog() {
    View mView = getLayoutInflater().inflate(R.layout.dialog_location_list, null);
    final Spinner mySpinner = mView.findViewById(R.id.location_spinner);
    AlertDialog.Builder mBuilder = new AlertDialog.Builder(BrowseActivity.this);

    mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        mLocationSort = mySpinner.getSelectedItem().toString();
        mCurrentFilter = FILTER_LOCATION;
        changeTabs();
      }
    });
    mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Log.d("ya", "stuff");
      }
    });

    mBuilder.setView(mView);

    final AlertDialog edit_dialog = mBuilder.create();
    edit_dialog.show();
  }

  // function will build alert dialog for user to choose their protein sort
  private void buildProteinSortDialog() {

    View mView = getLayoutInflater().inflate(R.layout.dialog_protein_list, null);
    final Spinner mySpinner = mView.findViewById(R.id.protein_spinner);
    AlertDialog.Builder mBuilder = new AlertDialog.Builder(BrowseActivity.this);
    mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        mProteinSort = mySpinner.getSelectedItem().toString();
        mCurrentFilter = FILTER_PROTEIN;
        changeTabs();
      }
    });
    mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
      }
    });

    mBuilder.setView(mView);
    final AlertDialog edit_dialog = mBuilder.create();
    edit_dialog.show();
  }

  private void createMealsActionButtons() {

    mSpeedDialView.addActionItem(
        new SpeedDialActionItem.Builder(R.id.fab_meal_location, R.drawable.ic_location_city_green_24dp)
            .setLabel("Location")
            .setLabelColor(Color.GRAY)
            .setLabelBackgroundColor(Color.WHITE)
            .setLabelClickable(false)
            .create());

    mSpeedDialView.addActionItem(
        new SpeedDialActionItem.Builder(R.id.fab_protein, R.drawable.ic_favorite_green_24dp)
            .setLabel("Protein")
            .setLabelColor(Color.GRAY)
            .setLabelBackgroundColor(Color.WHITE)
            .setLabelClickable(false)
            .create());
  }

  private void createPledgeActionButtons() {

    mSpeedDialView.addActionItem(
        new SpeedDialActionItem.Builder(R.id.fab_pledge_location, R.drawable.ic_location_city_green_24dp)
            .setLabel("Location")
            .setLabelColor(Color.GRAY)
            .setLabelBackgroundColor(Color.WHITE)
            .setLabelClickable(false)
            .create());

    mSpeedDialView.addActionItem(
        new SpeedDialActionItem.Builder(R.id.fab_biggest, R.drawable.ic_show_chart_green_24dp)
            .setLabel("Largest")
            .setLabelColor(Color.GRAY)
            .setLabelBackgroundColor(Color.WHITE)
            .setLabelClickable(false)
            .create());
  }

  private void startTabHost() {

    this.mTabHost = findViewById(R.id.tabhost_browse);
    this.mTabHost.setup();
  }

  private void startFragmentManager() {
    this.mFragmentManager = getSupportFragmentManager();
  }

  private void startFragment(Fragment fragmentToStart, int currentTabId) {
    startNavDrawer();
    FragmentTransaction newTransaction = mFragmentManager.beginTransaction();
    Bundle args = packageFilterPreferences();
    fragmentToStart.setArguments(args);
    newTransaction.replace(currentTabId, fragmentToStart);
    newTransaction.commit();
  }

  private Bundle packageFilterPreferences() {
    Bundle bundleToSend = new Bundle();
    bundleToSend.putInt("Filter", mCurrentFilter);
    switch (mCurrentFilter) {
      case FILTER_LOCATION:
        bundleToSend.putString("Location", mLocationSort);
        break;
      case FILTER_PROTEIN:
        bundleToSend.putString("Protein", mProteinSort);
        break;
    }
    return bundleToSend;
  }

  private void displayTabs() {

    startToolbar();
    this.startTabHost();
    this.createTab("Pledges", R.id.container_browse_pledges);
    this.createTab("Meals"    , R.id.container_browse_meals);
  }

  private void displayFragment() {


    this.startFragmentManager();
    Fragment currentFragment = this.getCurrentFragment();

    int currentTabId = this.getCurrentTabId();
    this.startFragment(currentFragment, currentTabId);
  }

  private void createTab(String newTabText, int newTabId) {

    TabHost.TabSpec newTab = mTabHost.newTabSpec(newTabText);
    newTab.setIndicator(newTabText);
    newTab.setContent(newTabId);
    this.mTabHost.addTab(newTab);
  }

  private void setTabListener() {
    mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
      public void onTabChanged(String tabId) {
        changeTabs();
        createFAB();
      }
    });
  }

  private void changeTabs() {
    int newTabId = getCurrentTabId();
    Log.d("Ye", String.valueOf(newTabId));
    Fragment fragmentToStart = this.getCurrentFragment();
    this.startFragment(fragmentToStart, newTabId);
    mCurrentFilter = FILTER_NONE;
  }

  private int getCurrentTabId() {
    int currentTab = mTabHost.getCurrentTab();
    if (currentTab == TAB_PLEDGES) {
      return R.id.container_browse_pledges;
    }
    return R.id.container_browse_meals;
  }

  private Fragment getCurrentFragment() {
    int currentTab = mTabHost.getCurrentTab();
    //noinspection UnnecessaryLocalVariable
    if (currentTab == TAB_PLEDGES) {
      return new BrowsePledgesTabFragment();
    }
    return new BrowseMealsTabFragment();
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mDrawerToggle.syncState();
  }
}