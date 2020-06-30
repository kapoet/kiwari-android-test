package com.ervin.kiwariandroidtest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ervin.kiwariandroidtest.data.ChatRepository;
import com.ervin.kiwariandroidtest.data.local.entity.ChatEntity;
import com.ervin.kiwariandroidtest.data.local.entity.UserEntity;
import com.ervin.kiwariandroidtest.data.model.Chat;
import com.ervin.kiwariandroidtest.data.remote.ApiResponse;
import com.ervin.kiwariandroidtest.data.remote.RemoteRepository;
import com.ervin.kiwariandroidtest.helpers.Utils;
import com.ervin.kiwariandroidtest.valueobject.Resource;

import java.util.List;

public class ChatsViewModel extends ViewModel {
    ChatRepository chatRepository;
    RemoteRepository remoteRepository;
    private MutableLiveData<String> chatKey = new MutableLiveData<>();

    public ChatsViewModel(ChatRepository chatRepository, RemoteRepository remoteRepository) {
        this.chatRepository = chatRepository;
        this.remoteRepository = remoteRepository;
    }

    public LiveData<ApiResponse<List<Chat>>> repoChat = Transformations.switchMap(chatKey, input -> remoteRepository.getChats(input));

    public LiveData<Resource<List<ChatEntity>>> getChats = Transformations.switchMap(repoChat, input -> chatRepository.getChats(chatKey.getValue()));

//    public LiveData<List<ChatEntity>> localChat = Transformations.switchMap(chatKey, input -> chatRepository.getChats(chatKey.getValue()));

    public void setChat(String CurrentUserUID, String firendUd) {
        chatKey.setValue(Utils.produceId(CurrentUserUID, firendUd));
    }

    public void insertChat(Chat chat, String friendUID) {
        chatRepository.insertChat(chat,friendUID);
    }

}
