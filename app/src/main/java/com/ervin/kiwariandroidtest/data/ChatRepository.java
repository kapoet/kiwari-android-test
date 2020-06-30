package com.ervin.kiwariandroidtest.data;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ervin.kiwariandroidtest.App;
import com.ervin.kiwariandroidtest.data.local.LocalRepository;
import com.ervin.kiwariandroidtest.data.local.entity.ChatEntity;
import com.ervin.kiwariandroidtest.data.local.entity.UserEntity;
import com.ervin.kiwariandroidtest.data.model.Chat;
import com.ervin.kiwariandroidtest.data.model.User;
import com.ervin.kiwariandroidtest.data.remote.ApiResponse;
import com.ervin.kiwariandroidtest.data.remote.RemoteRepository;
import com.ervin.kiwariandroidtest.helpers.AbsentLiveData;
import com.ervin.kiwariandroidtest.helpers.AppExecutors;
import com.ervin.kiwariandroidtest.helpers.Global;
import com.ervin.kiwariandroidtest.helpers.Utils;
import com.ervin.kiwariandroidtest.valueobject.Resource;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ChatRepository implements ChatDataSource {

    @Inject
    AppExecutors appExecutors;
    @Inject
    LocalRepository localRepository;
    @Inject
    RemoteRepository remoteRepository;

    @Inject
    public ChatRepository() {
        App.application.inject(this);
    }

    @Override
    public LiveData<Resource<List<UserEntity>>> getAllUser() {
        return new NetworkBoundResource<List<UserEntity>,List<User>>(appExecutors){

            @Override
            protected LiveData<List<UserEntity>> loadFromDB() {
                return localRepository.getAllUser();
            }

            @Override
            protected Boolean shouldFetch(List<UserEntity> data) {
                return true;
            }

            @Override
            protected LiveData<ApiResponse<List<User>>> createCall() {
                return remoteRepository.getAllUser();
            }

            @Override
            protected void saveCallResult(List<User> data) {
                for (User user : data) {
                    UserEntity userEntity = new UserEntity(user.getName(), user.getEmail(), user.getImgURL(), user.getUid());
                    localRepository.insertUser(userEntity);
                }
            }
        }.asLiveData();
    }

    @Override
    public LiveData<UserEntity> getCurrentUser() {
        return localRepository.getCurrentUSer();
    }

    @Override
    public LiveData<Resource<List<ChatEntity>>> getChats(String chatGroup) {
        return new NetworkBoundResource<List<ChatEntity>, List<Chat>>(appExecutors) {
            @Override
            protected LiveData<List<ChatEntity>> loadFromDB() {
                return localRepository.getAllChats(chatGroup);
            }

            @Override
            protected Boolean shouldFetch(List<ChatEntity> data) {
                return true;
            }

            @Override
            protected LiveData<ApiResponse<List<Chat>>> createCall() {
                return remoteRepository.getChats(chatGroup);
            }

            @Override
            protected void saveCallResult(List<Chat> data) {
                List<ChatEntity> chatEntities = new ArrayList<>();

                for (Chat chat : data) {
                    chatEntities.add(new ChatEntity(chatGroup,
                            chat.getSenderId(), chat.getSenderName(),
                            chat.getSenderImage(), chat.getMessage(), chat.getTimestamp(),chat.getChatID()));
                }

                localRepository.insertChat(chatEntities);
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<UserEntity>> login(String username, String password) {
        return new NetworkBoundResource<UserEntity, User>(appExecutors) {
            @Override
            protected LiveData<UserEntity> loadFromDB() {
                return localRepository.getSingleUser(username);
            }

            @Override
            protected Boolean shouldFetch(UserEntity data) {
                return true;
            }

            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return remoteRepository.login(username, password);
            }

            @Override
            protected void saveCallResult(User data) {
                UserEntity userEntity = new UserEntity(data.getName(), data.getEmail(), data.getImgURL(), data.getUid());
                userEntity.setCurrentUser(true);
                localRepository.insertUser(userEntity);
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<User>> signup(User user, String password, Uri uri, String filename) {
        return new NetworkBoundResource<User, User>(appExecutors) {
            @Override
            protected LiveData<User> loadFromDB() {
                return AbsentLiveData.create();
            }

            @Override
            protected Boolean shouldFetch(User data) {
                return true;
            }

            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return remoteRepository.signup(user, password, uri, filename);
            }

            @Override
            protected void saveCallResult(User data) {

            }
        }.asLiveData();
    }

    @Override
    public void insertChat(Chat chat, String friendUID) {
        List<ChatEntity> localChat = new ArrayList<>();
        List<Chat> remoteChat = new ArrayList<>();
        DatabaseReference databaseReference= App.application.getDatabaseReference().child(Global.CHATS)
                .child(Utils.produceId(chat.getSenderId(),friendUID))
                .push();
        String key = databaseReference.getKey();
        localChat.add(new ChatEntity(Utils.produceId(chat.getSenderId(),friendUID),
                chat.getSenderId(),
                chat.getSenderName(),
                chat.getSenderImage(),
                chat.getMessage(),
                chat.getTimestamp(),
                key));
        remoteChat.add(chat);
        remoteChat.get(0).setChatID(key);
        Runnable runnable = () -> localRepository.insertChat(localChat);
        appExecutors.diskIO().execute(runnable);
        remoteRepository.insertChat(remoteChat,databaseReference);
    }

    @Override
    public LiveData<ApiResponse<String>> uploadFile(Uri uri, String fileName) {
        return remoteRepository.uploadImage(uri,fileName);
    }


}
