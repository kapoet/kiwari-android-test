package com.ervin.kiwariandroidtest.data;

import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.ervin.kiwariandroidtest.data.local.entity.ChatEntity;
import com.ervin.kiwariandroidtest.data.local.entity.UserEntity;
import com.ervin.kiwariandroidtest.data.model.Chat;
import com.ervin.kiwariandroidtest.data.model.User;
import com.ervin.kiwariandroidtest.data.remote.ApiResponse;
import com.ervin.kiwariandroidtest.valueobject.Resource;

import java.util.List;

public interface ChatDataSource {
    LiveData<Resource<List<UserEntity>>> getAllUser();

    LiveData<UserEntity> getCurrentUser();

    LiveData<Resource<List<ChatEntity>>> getChats(String friendUID);

    LiveData<Resource<UserEntity>> login(String username, String password);

    LiveData<Resource<User>> signup(User user, String password, Uri uriFile, String fileName);

    void insertChat(Chat chat, String friendUID);

    LiveData<ApiResponse<String>> uploadFile(Uri uri, String fileName);
}
