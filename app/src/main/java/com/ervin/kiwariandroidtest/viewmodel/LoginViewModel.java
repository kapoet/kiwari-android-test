package com.ervin.kiwariandroidtest.viewmodel;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ervin.kiwariandroidtest.data.ChatRepository;
import com.ervin.kiwariandroidtest.data.local.entity.UserEntity;
import com.ervin.kiwariandroidtest.data.model.User;
import com.ervin.kiwariandroidtest.valueobject.Resource;

public class LoginViewModel extends ViewModel {

    private ChatRepository chatRepository;
    private String Username, Password;

    private MutableLiveData<String> _loginUser = new MutableLiveData<>();
    public LiveData<Resource<UserEntity>> loginUser = Transformations.switchMap(_loginUser, input -> loginUser = chatRepository.login(Username, Password));

    public void setChatRepo(ChatRepository chatRepo) {
        chatRepository = chatRepo;
    }

    public void setLogin(String username, String Password) {
        this.Username = username;
        this.Password = Password;
        _loginUser.setValue("anjing");
    }

    public LiveData<Resource<UserEntity>> getLoginUser() {
        return chatRepository.login(Username, Password);
    }

}
