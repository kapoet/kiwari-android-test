package com.ervin.kiwariandroidtest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ervin.kiwariandroidtest.data.ChatRepository;
import com.ervin.kiwariandroidtest.data.local.entity.UserEntity;
import com.ervin.kiwariandroidtest.data.remote.RemoteRepository;
import com.ervin.kiwariandroidtest.valueobject.Resource;

import java.util.List;

public class UserViewModel extends ViewModel {
    ChatRepository chatRepository;
    RemoteRepository remoteRepository;
    private MutableLiveData<String> _cuurentUser = new MutableLiveData<>();

    public UserViewModel(ChatRepository chatRepository, RemoteRepository remoteRepository) {
        this.chatRepository = chatRepository;
        this.remoteRepository = remoteRepository;
        _cuurentUser.setValue("anjing");
    }

    public LiveData<UserEntity> getCurrentUser = Transformations.switchMap(_cuurentUser, input -> chatRepository.getCurrentUser());

//    public LiveData<ApiResponse<List<User>>> _xx = Transformations.switchMap(_cuurentUser, input -> remoteRepository.getAllUser2());

    public LiveData<Resource<List<UserEntity>>> getAllUsers = Transformations.switchMap(_cuurentUser, input -> chatRepository.getAllUser());
}
