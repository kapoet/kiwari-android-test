package com.ervin.kiwariandroidtest.di;

import androidx.room.Room;

import com.ervin.kiwariandroidtest.App;
import com.ervin.kiwariandroidtest.data.local.ChatDAO;
import com.ervin.kiwariandroidtest.data.local.entity.ChatDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class RoomModule {
    private ChatDatabase chatDatabase;

    public RoomModule(App context) {
        chatDatabase =  Room.databaseBuilder(context,
                ChatDatabase.class, "Chats.db")
                .build();
    }

    @Singleton
    @Provides
    ChatDatabase provideChatDatabase() {
        return chatDatabase;
    }

    @Singleton
    @Provides
    ChatDAO provideChatDAO() {
        return chatDatabase.chatDAO();
    }
}
