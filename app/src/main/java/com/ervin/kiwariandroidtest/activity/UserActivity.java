package com.ervin.kiwariandroidtest.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.ervin.kiwariandroidtest.Global;
import com.ervin.kiwariandroidtest.Preferences;
import com.ervin.kiwariandroidtest.R;
import com.ervin.kiwariandroidtest.model.User;
import com.ervin.kiwariandroidtest.adapter.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<User> users;
    User currentUser;
    RecyclerView rvUser;
    UserAdapter userAdapter;
    private FirebaseAuth mAuth;
    int isFirstTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Friend to Chat");

        rvUser = findViewById(R.id.rv_users);
        rvUser.setHasFixedSize(true);
        rvUser.setLayoutManager(new LinearLayoutManager(this));
        rvUser.addItemDecoration(new DividerItemDecoration(rvUser.getContext(), DividerItemDecoration.VERTICAL));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();

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
        rvUser.setAdapter(userAdapter);

        myRef.child(Global.ACCOUNT).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if (!dataSnapshot.getKey().equals(Preferences.getCurrentUID(UserActivity.this))){
                    users.add(user);
                    userAdapter.notifyDataSetChanged();
                } else {
                    currentUser = user;
                }
                if (isFirstTime==0){
                    ProgressBar progressBar = findViewById(R.id.pb_loading_main);
                    progressBar.setVisibility(View.GONE);
                    isFirstTime = 1;
                }

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
