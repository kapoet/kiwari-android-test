package com.ervin.kiwariandroidtest.helpers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ervin.kiwariandroidtest.App;
import com.ervin.kiwariandroidtest.data.ChatRepository;
import com.ervin.kiwariandroidtest.data.remote.RemoteRepository;
import com.ervin.kiwariandroidtest.viewmodel.ChatsViewModel;
import com.ervin.kiwariandroidtest.viewmodel.SignupViewModel;
import com.ervin.kiwariandroidtest.viewmodel.UserViewModel;

import javax.inject.Inject;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    ChatRepository chatRepository;
    RemoteRepository remoteRepository;

    public ViewModelFactory(ChatRepository chatRepository, RemoteRepository remoteRepository) {
        this.chatRepository = chatRepository;
        this.remoteRepository = remoteRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignupViewModel.class)) {
            //noinspection unchecked
            return (T) new SignupViewModel(chatRepository);
        } else if (modelClass.isAssignableFrom(UserViewModel.class)) {
            //noinspection unchecked
            return (T) new UserViewModel(chatRepository,remoteRepository);
        } else if (modelClass.isAssignableFrom(ChatsViewModel.class)) {
            //noinspection unchecked
            return (T) new ChatsViewModel(chatRepository,remoteRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
