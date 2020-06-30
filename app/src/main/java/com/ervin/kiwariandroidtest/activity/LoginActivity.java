package com.ervin.kiwariandroidtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.amitshekhar.DebugDB;
import com.ervin.kiwariandroidtest.App;
import com.ervin.kiwariandroidtest.data.ChatRepository;
import com.ervin.kiwariandroidtest.data.model.User;
import com.ervin.kiwariandroidtest.data.remote.RemoteRepository;
import com.ervin.kiwariandroidtest.helpers.Preferences;
import com.ervin.kiwariandroidtest.R;
import com.ervin.kiwariandroidtest.viewmodel.LoginViewModel;
import com.ervin.kiwariandroidtest.viewmodel.SignupViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;
    TextInputLayout tilEmail, tilPassword;
    String email, password;
    LoginViewModel loginViewModel;
    @Inject
    ChatRepository chatRepository;
    @Inject
    RemoteRepository remoteRepository;

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
        btnRegister = findViewById(R.id.btn_register);
        tilEmail = findViewById(R.id.txt_email);
        tilPassword = findViewById(R.id.txt_password);

        App.application.inject(this);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.setChatRepo(chatRepository);

        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        });
        loginViewModel.loginUser.observe(LoginActivity.this, userResource -> {
            if (userResource != null) {
                switch (userResource.status) {
                    case LOADING:
//                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        Log.d("Berhasil", "hhh: " + (userResource.data != null ? userResource.data.getUid() : "jangan"));
                        break;
                    case ERROR:
//                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });
        Log.d("Address databse", "onCreate: " + DebugDB.getAddressLog());
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = tilEmail.getEditText().getText().toString();
                password = tilPassword.getEditText().getText().toString();
                if (email.length() < 1 || password.length() < 1) {
                    Toast.makeText(LoginActivity.this, "Please check your input", Toast.LENGTH_LONG).show();
                } else {
                    loginViewModel.setLogin(email, password);
                }
            }
        });
    }
}
