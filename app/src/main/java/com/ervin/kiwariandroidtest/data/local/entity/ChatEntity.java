package com.ervin.kiwariandroidtest.data.local.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chatentities")
public class ChatEntity {

    @ColumnInfo
    private String chatGroup;
    @ColumnInfo
    private String senderId;
    @ColumnInfo
    private String senderName;
    @ColumnInfo
    private String senderImage;
    @ColumnInfo
    private String message;
    @ColumnInfo
    private long timestamp;
    @PrimaryKey
    @NonNull
    @ColumnInfo
    private String chatID;

    public ChatEntity() {
    }

    public ChatEntity(String chatGroup, String senderId, String senderName, String senderImage, String message, long timestamp, String chatID) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderImage = senderImage;
        this.message = message;
        this.timestamp = timestamp;
        this.chatGroup = chatGroup;
        this.chatID = chatID;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getChatGroup() {
        return chatGroup;
    }

    public void setChatGroup(String chatGroup) {
        this.chatGroup = chatGroup;
    }

    @NonNull
    public String getChatID() {
        return chatID;
    }

    public void setChatID(@NonNull String chatID) {
        this.chatID = chatID;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        ChatEntity entry = (ChatEntity) obj;
        return chatID.equals(entry.getChatID());
    }
}
