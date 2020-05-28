package com.ervin.kiwariandroidtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ervin.kiwariandroidtest.model.Chat;
import com.ervin.kiwariandroidtest.adapter.ChatAdapter;
import com.ervin.kiwariandroidtest.Global;
import com.ervin.kiwariandroidtest.Preferences;
import com.ervin.kiwariandroidtest.R;
import com.ervin.kiwariandroidtest.model.User;
import com.ervin.kiwariandroidtest.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    RecyclerView rvChat;
    EditText etChat;
    FloatingActionButton fabChat;
    ChatAdapter chatAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    List<Chat> listChat;
    Chat chat;
    User userToChat, currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userToChat = getIntent().getParcelableExtra(Global.ACCOUNT);
        currentUser = getIntent().getParcelableExtra(Global.CURRENT_USER);

        setTitle(userToChat.getName());

        etChat = findViewById(R.id.et_chat);
        fabChat = findViewById(R.id.fab_send);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        rvChat = findViewById(R.id.rv_chat);
        rvChat.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(linearLayoutManager);

        listChat = new ArrayList<>();
        chatAdapter = new ChatAdapter(listChat, this);
        rvChat.setAdapter(chatAdapter);

        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etChat.getText().toString();
                if (message.length()>0){
                    chat = new Chat(currentUser.getUid(),currentUser.getName(),
                            currentUser.getImgURL(),message,System.currentTimeMillis());
                    myRef.child(Global.CHATS)
                            .child(Utils.produceId(currentUser.getUid(), userToChat.getUid()))
                            .push()
                            .setValue(chat);
                    etChat.setText("");
                } else {
                    Toast.makeText(ChatActivity.this, "Cannot send empty message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(Global.CHATS)
                .child(Utils.produceId(currentUser.getUid(),userToChat.getUid()));
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                listChat.add(chat);
                chatAdapter.notifyDataSetChanged();
                rvChat.scrollToPosition(listChat.size()-1);
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
                Preferences.setLoggedIn(ChatActivity.this,false,"");
                Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
