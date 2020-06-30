package com.ervin.kiwariandroidtest.data.local.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "userentities")
public class UserEntity implements Parcelable {

    @ColumnInfo
    private String name;

    @ColumnInfo
    @PrimaryKey
    @NonNull
    private String email;

    @ColumnInfo
    private String imgURL;

    @ColumnInfo
    private String uid;

    @ColumnInfo
    private boolean isCurrentUser;

    public UserEntity(String name, String email, String imgURL, String uid) {
        this.name = name;
        this.email = email;
        this.imgURL = imgURL;
        this.uid = uid;
        this.isCurrentUser = false;
    }

    public UserEntity() {
    }

    public boolean isCurrentUser() {
        return isCurrentUser;
    }

    public void setCurrentUser(boolean currentUser) {
        isCurrentUser = currentUser;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.imgURL);
        dest.writeString(this.uid);
    }

    protected UserEntity(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.imgURL = in.readString();
        this.uid = in.readString();
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel source) {
            return new UserEntity(source);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
//        return super.equals(obj);
        UserEntity entry = (UserEntity) obj;
        return email.equals(entry.getEmail());
    }
}
