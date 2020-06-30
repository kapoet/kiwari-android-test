package com.ervin.kiwariandroidtest.viewmodel;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ervin.kiwariandroidtest.App;
import com.ervin.kiwariandroidtest.data.ChatRepository;
import com.ervin.kiwariandroidtest.data.model.User;
import com.ervin.kiwariandroidtest.data.remote.ApiResponse;
import com.ervin.kiwariandroidtest.helpers.AbsentLiveData;
import com.ervin.kiwariandroidtest.valueobject.Resource;


public class SignupViewModel extends ViewModel {

    ChatRepository chatRepository;
    private MutableLiveData<String> _signupUser = new MutableLiveData<>();

    private MutableLiveData<ApiResponse<String>> _uploadImage = new MutableLiveData<>();

    public SignupViewModel(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    private User user;
    private String password, filename;
    private Uri urifile;

    public LiveData<Resource<User>> signupUser = Transformations.switchMap(_signupUser, input ->
            chatRepository.signup(user, password, urifile, filename));

    public void signup(User user, String password, Uri uri, String filename) {
        this.user = user;
        this.password = password;
        urifile = uri;
        this.filename = filename;
        _signupUser.setValue("anjing");
    }
}



