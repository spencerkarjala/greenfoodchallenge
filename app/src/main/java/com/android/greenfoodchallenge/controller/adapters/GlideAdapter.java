package com.android.greenfoodchallenge.controller.adapters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

/*
 *  Module that Glide looks at to unpack StorageReferences
 *  to dynamically load images from Firebase Storage
 */

@GlideModule
public class GlideAdapter extends AppGlideModule {

  @Override
  public void registerComponents(@NonNull Context context,
                                 @NonNull Glide glide,
                                 @NonNull Registry registry) {
    registry.append(StorageReference.class, InputStream.class, new FirebaseImageLoader.Factory());
  }
}
