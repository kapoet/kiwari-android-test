package com.ervin.kiwariandroidtest.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.ervin.kiwariandroidtest.App;
import com.ervin.kiwariandroidtest.R;
import com.ervin.kiwariandroidtest.data.ChatRepository;
import com.ervin.kiwariandroidtest.data.model.User;
import com.ervin.kiwariandroidtest.databinding.ActivitySignupBinding;
import com.ervin.kiwariandroidtest.helpers.ViewModelFactory;
import com.ervin.kiwariandroidtest.viewmodel.SignupViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;

import javax.inject.Inject;

public class SignupActivity extends AppCompatActivity {
    @Inject
    ChatRepository chatRepository;
    SignupViewModel signedUpViewModel;
    ActivitySignupBinding binding;
    User user;
    Uri fileUri;
    String filePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.application.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        ViewModelFactory viewModelFactory = new ViewModelFactory(chatRepository,null);
        signedUpViewModel = new ViewModelProvider(this, viewModelFactory).get(SignupViewModel.class);
        user = new User();
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.txtName.getEditText().getText().toString().length() > 0
                        && binding.txtEmail.getEditText().getText().toString().length() > 0
                        && binding.txtPassword.getEditText().getText().toString().length() > 0
                        && filePath != null) {

                    user.setEmail(binding.txtEmail.getEditText().getText().toString());
                    user.setName(binding.txtName.getEditText().getText().toString());

                    String extension = filePath.substring(filePath.lastIndexOf("."));

                    signedUpViewModel.signup(user, binding.txtPassword.getEditText().getText().toString(), fileUri, user.getEmail() + extension);
                    signedUpViewModel.signupUser.observe(SignupActivity.this, userResource -> {
                        if (userResource != null) {
                            switch (userResource.status) {
                                case LOADING:
                                    binding.pbLoading.setVisibility(View.VISIBLE);
                                    break;
                                case SUCCESS:
                                    binding.pbLoading.setVisibility(View.GONE);
                                    //kalau userresource nya kosong maka saveCAllResult pada chatRepo musti diisi
                                    Log.d("Berhasil", "taii: " + (userResource.data != null ? userResource.data.getUid() : "jangan"));
                                    break;
                                case ERROR:
                                    binding.pbLoading.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                }
            }
        });

        binding.ivImagePicker.setOnClickListener(view -> {
            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri = data.getData();
            Glide.with(this)
                    .load(fileUri)
                    .circleCrop()
                    .into(binding.ivImagePicker);


            File file = ImagePicker.Companion.getFile(data);

            //You can also get File Path from intent
            filePath = ImagePicker.Companion.getFilePath(data);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
//            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}
