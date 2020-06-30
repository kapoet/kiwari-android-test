package com.ervin.kiwariandroidtest.data.local;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ervin.kiwariandroidtest.data.local.entity.ChatEntity;
import com.ervin.kiwariandroidtest.data.local.entity.UserEntity;
import com.ervin.kiwariandroidtest.data.model.Chat;
import com.ervin.kiwariandroidtest.helpers.KotlinHelper;

import java.util.List;

@Dao
public interface ChatDAO {

    @WorkerThread
    @Query("SELECT * FROM chatentities where chatGroup = :chatGroup")
    LiveData<List<ChatEntity>> getChats(String chatGroup);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChats(List<ChatEntity> chats);

    @WorkerThread
    @Query("SELECT * FROM userentities")
    LiveData<List<UserEntity>> getUsers();

    @WorkerThread
    @Query("SELECT * FROM userentities where email = :mail")
    LiveData<UserEntity> getSingleUser(String mail);

    @WorkerThread
    @Query("SELECT * FROM userentities where isCurrentUser = 1")
    LiveData<UserEntity> getCurrentUser();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUsers(UserEntity user);
}
