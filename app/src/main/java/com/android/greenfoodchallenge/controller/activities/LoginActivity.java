/*
  Copyright 2016 Google Inc. All Rights Reserved.
  Modified

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package com.android.greenfoodchallenge.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.android.greenfoodchallenge.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/*
 *  contains logic to display login screen and allow user's Google login
 */

public class LoginActivity extends BaseActivity {

  private final int RC_SIGN_IN = 1338;
  private final String TAG = "LoginActivity";
  private final Context currentContext = this;

  private GestureDetectorCompat mGestureDetector;
  private FirebaseAuth mFireAuth;
  private GoogleSignInOptions mGoogleLoginOptions;
  private GoogleSignInClient mGoogleLoginClient;
  private GoogleApiClient mGoogleApiClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    mCurrentActivity = LoginActivity.class;

    mGestureDetector = new GestureDetectorCompat(this, new GestureListener());
    SignInButton mLoginButton = findViewById(R.id.loginButton);
    mLoginButton.setSize(SignInButton.SIZE_STANDARD);

    mLoginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        tryGoogleSignIn();
      }
    });

    fetchDataFromIntent();

  }

  @Override
  protected void onStart() {
    super.onStart();

    this.createGoogleLogin();
    this.createFirebaseAuth();
    this.checkFirebaseSignedIn();
  }

  private void createGoogleLogin() {
    this.createGoogleLoginOptions();
    this.createGoogleLoginClient();
  }

  private void createGoogleLoginClient() {
    mGoogleLoginClient = GoogleSignIn.getClient(this, mGoogleLoginOptions);
  }

  private void tryGoogleSignIn() {
    Intent signInIntent = mGoogleLoginClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  private void createGoogleLoginOptions() {
    mGoogleLoginOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build();

    if (mGoogleApiClient == null) {
      mGoogleApiClient = new GoogleApiClient.Builder(this)
          .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
              /* do stuff i guess */
            }
          })
          .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleLoginOptions)
          .build();
    }
  }

  private void createFirebaseAuth() {
    this.mFireAuth = FirebaseAuth.getInstance();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    mGestureDetector.onTouchEvent(event);
    return super.onTouchEvent(event);
  }

  private void checkFirebaseSignedIn() {
      FirebaseUser currentUser = mFireAuth.getCurrentUser();
      if (currentUser != null) {
        moveToActivity(this, MainActivity.class);
      }
  }

  class GestureListener extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onDown(MotionEvent event) {
      return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {

      float deltaY = event2.getY() - event1.getY();
      if (deltaY > 100) {
        moveToActivity(currentContext, SplashActivity.class);
      }
      return true;
    }
  }

  /**
   * everything below this is modified Google code from https://tinyurl.com/yblazaky
   */

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.d(TAG, "Login returned");

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        // Google Sign In was successful, authenticate with Firebase
        GoogleSignInAccount account = task.getResult(ApiException.class);
        firebaseAuthWithGoogle(account);
      } catch (ApiException e) {
        // Google Sign In failed, update UI appropriately
        Log.w(TAG, "Google sign in failed", e);
        // ...
      }
    }
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
    Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

    AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    mFireAuth.signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              // Sign in success, update UI with the signed-in user's information
              Log.d(TAG, "signInWithCredential:success");
              FirebaseUser user = mFireAuth.getCurrentUser();
              moveToActivity(currentContext, MainActivity.class);
            } else {
              // If sign in fails, display a message to the user.
              Log.w(TAG, "signInWithCredential:failure", task.getException());
            }
            // ...
          }
        });
  }
}