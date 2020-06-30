package com.ervin.kiwariandroidtest.data.local.entity;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ervin.kiwariandroidtest.data.local.ChatDAO;

@Database(entities = {ChatEntity.class, UserEntity.class}, version = 1)
public abstract class ChatDatabase extends RoomDatabase {
//    private static ChatDatabase INSTANCE;

    public abstract ChatDAO chatDAO();

//    private static final Object sLock = new Object();
//
//    public static ChatDatabase getInstance(Context context) {
//        synchronized (sLock) {
//            if (INSTANCE == null) {
//                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                        ChatDatabase.class, "Chats.db")
//                        .build();
//            }
//            return INSTANCE;
//        }
//    }
}
