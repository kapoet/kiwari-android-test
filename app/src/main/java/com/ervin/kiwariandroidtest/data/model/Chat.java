package com.ervin.kiwariandroidtest.data.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Chat {
    private String senderId,
            senderName,
            senderImage,
            message,
            chatID;
    private long timestamp;

    public Chat() {
    }

    public Chat(String senderId, String senderName, String senderImage, String message, long timestamp) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderImage = senderImage;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
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
}
