package com.ervin.kiwariandroidtest.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ervin.kiwariandroidtest.App;
import com.ervin.kiwariandroidtest.data.ChatRepository;
import com.ervin.kiwariandroidtest.data.local.entity.UserEntity;
import com.ervin.kiwariandroidtest.data.remote.RemoteRepository;
import com.ervin.kiwariandroidtest.databinding.ActivityMainBinding;
import com.ervin.kiwariandroidtest.helpers.Global;
import com.ervin.kiwariandroidtest.helpers.Preferences;
import com.ervin.kiwariandroidtest.R;
import com.ervin.kiwariandroidtest.data.model.User;
import com.ervin.kiwariandroidtest.adapter.UserAdapter;
import com.ervin.kiwariandroidtest.helpers.ViewModelFactory;
import com.ervin.kiwariandroidtest.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class UserActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<User> users;
    User currentUser;
    UserAdapter userAdapter;
    @Inject
    FirebaseAuth mAuth;
    ActivityMainBinding binding;
    UserViewModel userViewModel;
    @Inject
    ChatRepository chatRepository;
    @Inject
    RemoteRepository remoteRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setTitle("Friend to Chat");
        App.application.inject(this);
        binding.rvUsers.setHasFixedSize(true);
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvUsers.addItemDecoration(new DividerItemDecoration(binding.rvUsers.getContext(), DividerItemDecoration.VERTICAL));

//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();
//        mAuth = FirebaseAuth.getInstance();

        users = new ArrayList<>();
        userAdapter = new UserAdapter(users, UserActivity.this, new UserAdapter.clickListener() {
            @Override
            public void onItemClick(User user) {
                Intent intent = new Intent(UserActivity.this, ChatActivity.class);
                intent.putExtra(Global.ACCOUNT, user);
                intent.putExtra(Global.CURRENT_USER, currentUser);
                startActivity(intent);
            }
        });
        binding.rvUsers.setAdapter(userAdapter);
        ViewModelFactory viewModelFactory = new ViewModelFactory(chatRepository,remoteRepository);
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.getCurrentUser.observe(this,userEntity -> {
            if (userEntity != null) {
                currentUser = new User(userEntity.getName(), userEntity.getEmail(), userEntity.getImgURL(), userEntity.getUid());
            }
        });
//        userViewModel._xx.observe(this,listApiResponse -> {});
        userViewModel.getAllUsers.observe(this,listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING:
                        binding.pbLoadingMain.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.pbLoadingMain.setVisibility(View.GONE);
                        for (UserEntity user : listResource.data) {
                            User _user = new User(user.getName(), user.getEmail(), user.getImgURL(), user.getUid());
                            if (!users.contains(_user)){
                                Log.d("Berhasil", "onCreate: jancok");
                                users.add(_user);
                            }
                            userAdapter.notifyDataSetChanged();
                        }
                        //kalau userresource nya kosong maka saveCAllResult pada chatRepo musti diisi
                        Log.d("Berhasil", "taii: " + (listResource.data != null ? listResource.data.get(0).getEmail() : "jangan"));
                        break;
                    case ERROR:
                        binding.pbLoadingMain.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
//        myRef.child(Global.ACCOUNT).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                User user = dataSnapshot.getValue(User.class);
//                if (!dataSnapshot.getKey().equals(Preferences.getCurrentUID(UserActivity.this))){
//                    users.add(user);
//                    userAdapter.notifyDataSetChanged();
//                } else {
//                    currentUser = user;
//                }
//                if (isFirstTime==0){
//                    ProgressBar progressBar = findViewById(R.id.pb_loading_main);
//                    progressBar.setVisibility(View.GONE);
//                    isFirstTime = 1;
//                }
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();
                }
                Preferences.setLoggedIn(UserActivity.this,false,"");
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
