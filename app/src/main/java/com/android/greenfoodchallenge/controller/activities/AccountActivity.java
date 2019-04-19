package com.android.greenfoodchallenge.controller.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.greenfoodchallenge.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

/*
 *  Displays user information, enables database permission
 *  configuration, and allows the user to delete their data.
 */

public class AccountActivity extends BaseActivity {

  private final String TAG = "AccountActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_account);

    fetchDataFromIntent();
    startToolbar();
    startDatabase();
    startNavDrawer();
    startProgressBar();
    setTitle(getString(R.string.toolbar_title_account));
  }

  private void startProgressBar() {
    findViewById(R.id.layout_account_container).setVisibility(View.INVISIBLE);
    findViewById(R.id.loading_icon).setVisibility(View.VISIBLE);
  }

  @Override
  protected void onStart() {
    super.onStart();
    this.getUserPermissionsFromDatabase();
  }

  private void refreshUi(boolean userNamePermission) {
    this.createButtons();
    this.createHeader();
    this.createCheckbox(userNamePermission);
  }

  private void stopProgressBar() {
    findViewById(R.id.loading_icon).setVisibility(View.INVISIBLE);
    findViewById(R.id.layout_account_container).setVisibility(View.VISIBLE);
  }

  private void createButtons() {
    Button buttonPermissions = findViewById(R.id.button_account_permissions);
    Button buttonRemoveData  = findViewById(R.id.button_account_data);

    buttonPermissions.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        saveUserPermissions();
      }
    });

    buttonRemoveData.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showRemoveDataAlert();
      }
    });
  }

  private void createCheckbox(boolean userNamePermission) {
    CheckBox checkBoxPermissions = findViewById(R.id.checkbox_account_permissions);

    try {
      checkBoxPermissions.setChecked(userNamePermission);
    } catch(NullPointerException e) {
      Log.d(TAG, "createCheckbox failed: " + e.getMessage());
    }
  }

  private void createHeader() {
    TextView textViewUserName  = findViewById(R.id.text_account_name);
    TextView textViewUserEmail = findViewById(R.id.text_account_email);
    FirebaseUser currentFireUser = this.getCurrentFirebaseUser();

    try {
      //noinspection ConstantConditions
      String fireUserName  = currentFireUser.getDisplayName();
      String fireUserEmail = currentFireUser.getEmail();
      textViewUserName.setText(fireUserName);
      textViewUserEmail.setText(fireUserEmail);
    } catch(NullPointerException e) {
      Log.d(TAG, "createHeader failed: " + e.getMessage());
    }
  }

  private FirebaseUser getCurrentFirebaseUser() {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    return firebaseAuth.getCurrentUser();
  }

  private String getCurrentUserName() {
    FirebaseUser currentFirebaseUser = this.getCurrentFirebaseUser();
    String currentUserName = currentFirebaseUser.getDisplayName();
    try {
      //noinspection ConstantConditions
      String[] nameSplit = currentUserName.split(" ");
      currentUserName = nameSplit[0] + " " + nameSplit[1].substring(0, 1);
    } catch(NullPointerException e) {
      Log.d(TAG, "getCurrentUserName failed: " + e.getMessage());
    }
    return currentUserName;
  }

  private void getUserPermissionsFromDatabase() {
    FirebaseUser currentFireUser = this.getCurrentFirebaseUser();
    mFireDatabase.collection("users")
            .document(currentFireUser.getUid())
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                boolean userNamePermission = false;
                try {
                  //noinspection ConstantConditions
                  userNamePermission = documentSnapshot.getBoolean("namePermission");
                } catch(NullPointerException e) {
                  Log.d(TAG, "getUserPermissionsFromDatabase failed: " + e.getMessage());
                }
                stopProgressBar();
                refreshUi(userNamePermission);
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "getUserPermissionsFromDatabase failed: " + e.getMessage());
              }
            });
  }

  private void saveUserPermissions() {
    CheckBox checkBoxPermissions = findViewById(R.id.checkbox_account_permissions);
    final boolean newUserNamePermission = checkBoxPermissions.isChecked();
    FirebaseUser currentFireUser = this.getCurrentFirebaseUser();
    HashMap<String, Object> userUpdateMap = new HashMap<>();

    userUpdateMap.put("namePermission", newUserNamePermission);
    //noinspection PointlessBooleanExpression
    if (newUserNamePermission == false) {
      userUpdateMap.put("name", "Anonymous");
    } else {
      userUpdateMap.put("name", getCurrentUserName());
    }

    mFireDatabase.collection("users")
            .document(currentFireUser.getUid())
            .update(userUpdateMap)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                refreshUi(newUserNamePermission);
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "saveUserPermissions failed: " + e.getMessage());
              }
            });
  }

  private void removeUserData() {
    this.startProgressBar();
    FirebaseUser currentFireUser = this.getCurrentFirebaseUser();
    mFireDatabase.collection("users")
            .document(currentFireUser.getUid())
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                removePledgeData();
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "removeUserData failed: " + e.getMessage());
              }
            });
  }

  private void removePledgeData() {
    final Context currentContext = this;
    FirebaseUser currentFireUser = this.getCurrentFirebaseUser();
    mFireDatabase.collection("pledges")
            .document(currentFireUser.getUid())
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                moveToActivity(currentContext, MainActivity.class);
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "removePledgeData failed: " + e.getMessage());
              }
            });
  }

  private void showRemoveDataAlert() {
    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    alertBuilder.setTitle("Remove data?");
    alertBuilder.setMessage("This can't be undone!");
    alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        removeUserData();
      }
    });
    alertBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        //close the alert
      }
    });
    final AlertDialog removeDataDialog = alertBuilder.create();
    removeDataDialog.show();
  }
}