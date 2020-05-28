package com.ervin.kiwariandroidtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ervin.kiwariandroidtest.Preferences;
import com.ervin.kiwariandroidtest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    TextInputLayout tilEmail, tilPassword;
    String email, password;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Preferences.isLoggedIn(LoginActivity.this)) {
            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
            startActivity(intent);
            finish();
        } else {
            ProgressBar pbLoading = findViewById(R.id.pb_loading);
            pbLoading.setVisibility(View.GONE);
        }

        btnLogin = findViewById(R.id.btn_login);
        tilEmail = findViewById(R.id.txt_email);
        tilPassword = findViewById(R.id.txt_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = tilEmail.getEditText().getText().toString();
                password = tilPassword.getEditText().getText().toString();
                if (email.length()<1 || password.length()<1){
                    Toast.makeText(LoginActivity.this,"Please check your input",Toast.LENGTH_LONG).show();
                } else {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Preferences.setLoggedIn(LoginActivity.this,true, FirebaseAuth.getInstance().getUid());
                                        Toast.makeText(LoginActivity.this,"Success",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });
    }
}
