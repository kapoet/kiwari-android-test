package com.ervin.kiwariandroidtest.di;

import com.ervin.kiwariandroidtest.activity.ChatActivity;
import com.ervin.kiwariandroidtest.activity.LoginActivity;
import com.ervin.kiwariandroidtest.activity.SignupActivity;
import com.ervin.kiwariandroidtest.activity.UserActivity;
import com.ervin.kiwariandroidtest.data.ChatRepository;
import com.ervin.kiwariandroidtest.data.local.ChatDAO;
import com.ervin.kiwariandroidtest.helpers.ViewModelFactory;
import com.ervin.kiwariandroidtest.viewmodel.SignupViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {
        AppModule.class,
        FirebaseModule.class,
        RoomModule.class})
@Singleton
public interface AppComponent {
    public void inject(UserActivity userActivity);

    public void inject(LoginActivity loginActivity);

    public void inject(ChatActivity chatActivity);

    public void inject(ChatRepository chatRepository);

    public void inject(SignupActivity signupActivity);

    public void inject(ViewModelFactory viewModelFactory);


    DatabaseReference getDatabaseReference();

    FirebaseAuth getFirebaseAuth();

    StorageReference getStorageReference();

    ChatDAO getChatDAO();

}
