package com.ervin.kiwariandroidtest.data.local;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.ervin.kiwariandroidtest.App;
import com.ervin.kiwariandroidtest.data.local.entity.ChatEntity;
import com.ervin.kiwariandroidtest.data.local.entity.UserEntity;
import com.ervin.kiwariandroidtest.helpers.KotlinHelper;

import java.util.List;

import javax.inject.Inject;


public class LocalRepository {

    private ChatDAO chatDao;
    @Inject
    public LocalRepository() {
        chatDao = App.application.getChatDAO();
    }

    public LiveData<List<ChatEntity>> getAllChats(String chatkey){
        return chatDao.getChats(chatkey);
    }

    public void insertChat(List<ChatEntity> Chats){
        chatDao.insertChats(Chats);
    }

    public LiveData<List<UserEntity>> getAllUser(){
        return chatDao.getUsers();
    }

    public LiveData<UserEntity> getSingleUser(String mail){
        return Transformations.distinctUntilChanged(chatDao.getSingleUser(mail));
    }

    public void insertUser(UserEntity user){
        chatDao.insertUsers(user);
    }

    public LiveData<UserEntity> getCurrentUSer() {
        return chatDao.getCurrentUser();
    }

}
