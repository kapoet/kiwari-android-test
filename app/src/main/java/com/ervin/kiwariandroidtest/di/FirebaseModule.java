package com.ervin.kiwariandroidtest.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {
    @Singleton
    @Provides
    public FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    public DatabaseReference provideDatabaseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Provides
    @Singleton
    public StorageReference provideStorageRef() {
        return FirebaseStorage.getInstance().getReference();
    }
}
