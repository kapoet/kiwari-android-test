package com.ervin.kiwariandroidtest.data.remote;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ervin.kiwariandroidtest.App;
import com.ervin.kiwariandroidtest.activity.LoginActivity;
import com.ervin.kiwariandroidtest.activity.UserActivity;
import com.ervin.kiwariandroidtest.data.local.entity.ChatEntity;
import com.ervin.kiwariandroidtest.data.model.Chat;
import com.ervin.kiwariandroidtest.data.model.User;
import com.ervin.kiwariandroidtest.helpers.Global;
import com.ervin.kiwariandroidtest.helpers.Preferences;
import com.ervin.kiwariandroidtest.helpers.Utils;
import com.ervin.kiwariandroidtest.valueobject.Resource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RemoteRepository {

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private StorageReference sRef;

    @Inject
    public RemoteRepository() {
        this.mAuth = App.application.getFirebaseAuth();
        this.mRef = App.application.getDatabaseReference();
        this.sRef = App.application.getStorageReference();
    }


    public LiveData<ApiResponse<List<User>>> getAllUser() {
        MutableLiveData<ApiResponse<List<User>>> resultUsers = new MutableLiveData<>();
        mRef.child(Global.ACCOUNT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Berhasil", "onDataChange: ");
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if (!dataSnapshot.getKey().equals(mAuth.getCurrentUser().getUid())) {
                        users.add(user);
                    }
                }
                resultUsers.setValue(ApiResponse.success(users));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                resultUsers.setValue(ApiResponse.empty("No Data Found", null));
            }
        });
        return resultUsers;
    }

    public LiveData<ApiResponse<List<User>>> getAllUser2() {
        ArrayList<User> users = new ArrayList<>();
        MutableLiveData<ApiResponse<List<User>>> resultUsers = new MutableLiveData<>();
        mRef.child(Global.ACCOUNT).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("Berhasil", "onDataChange: ");
                User user = dataSnapshot.getValue(User.class);
                    if (!dataSnapshot.getKey().equals(mAuth.getCurrentUser().getUid())) {
                        users.add(user);

                }
                resultUsers.setValue(ApiResponse.success(users));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return resultUsers;
    }

    public LiveData<ApiResponse<List<Chat>>> getChats(String chatKey) {
        MutableLiveData<ApiResponse<List<Chat>>> resultChats = new MutableLiveData<>();
        ArrayList<Chat> listChats = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(Global.CHATS)
                .child(chatKey);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                listChats.add(chat);
                resultChats.setValue(ApiResponse.success(listChats));
//                chatAdapter.notifyDataSetChanged();
//                rvChat.scrollToPosition(listChat.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addChildEventListener(childEventListener);
        return resultChats;
    }

    public void insertChat(List<Chat> chat, DatabaseReference databaseReference) {
        Chat singleChat = chat.get(0);
        databaseReference.setValue(singleChat);
    }


    public LiveData<ApiResponse<User>> login(String email, String password) {
        MutableLiveData<ApiResponse<User>> currentUser = new MutableLiveData<>();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser fUser = mAuth.getCurrentUser();
                            User user = new User(fUser.getDisplayName(),
                                    fUser.getEmail(),
                                    fUser.getPhotoUrl().toString(),
                                    fUser.getUid());
                            currentUser.postValue(ApiResponse.success(user));
                            Log.d("TAG", "Firebaseno: berhasil " + user.getUid());
                        } else {
                            currentUser.postValue(ApiResponse.empty("Error", null));
                            Log.d("TAG", "Firebaseno: gagal");

                        }

                    }
                });
        return currentUser;
    }

    public LiveData<ApiResponse<User>> signup(User user, String password, Uri Uri, String fileName) {
        MutableLiveData<ApiResponse<User>> currentUser = new MutableLiveData<>();
        StorageReference riversRef = sRef.child("images/" + fileName);
        riversRef.putFile(Uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riversRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<android.net.Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<android.net.Uri> task) {
                                if (task.isSuccessful()) {
                                    String generatedFilePath = task.getResult().toString();
                                    user.setImgURL(generatedFilePath);
                                } else {
                                    user.setImgURL("mondo");
                                }
                            }
                        });
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(), password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser fUser = mAuth.getCurrentUser();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(user.getName())
                                                    .setPhotoUri(Uri.parse(user.getImgURL()))
                                                    .build();
                                            fUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    user.setUid(fUser.getUid());
                                                    mRef.child(Global.ACCOUNT).child(fUser.getUid()).setValue(user);
                                                    User usera = new User(fUser.getDisplayName(), fUser.getEmail(), user.getImgURL(), fUser.getUid());
                                                    Log.d("Berhasil", "onComplete: babi");
                                                    currentUser.setValue(ApiResponse.success(usera));
                                                }
                                            });

                                        } else {
                                            currentUser.setValue(ApiResponse.empty("Error", null));
                                        }

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Berhasil", "onComplete: gagal " + e.getMessage());
                                        currentUser.setValue(ApiResponse.empty("Error", null));
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Berhasil", "onSuccess: gagalupload");
                    }
                });

        return currentUser;
    }

    public LiveData<ApiResponse<String>> uploadImage(Uri Uri, String fileName) {
        MutableLiveData<ApiResponse<String>> currentUrl = new MutableLiveData<>();

        StorageReference riversRef = sRef.child("images/" + fileName);

        riversRef.putFile(Uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUrl = taskSnapshot.getUploadSessionUri().toString();
                        currentUrl.setValue(ApiResponse.success(downloadUrl));
                        Log.d("Berhasil", "onSuccess: urlnya " + currentUrl.getValue().body);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Berhasil", "onSuccess: gagalupload");

                        currentUrl.setValue(ApiResponse.error("gagal upload gambar", e.getMessage()));
                    }
                });
        return currentUrl;
    }
}
