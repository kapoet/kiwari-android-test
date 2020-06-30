package com.ervin.kiwariandroidtest.data.model;

import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {
    private String name, email, imgURL, uid;

    public User(String name, String email, String imgURL, String uid) {
        this.name = name;
        this.email = email;
        this.imgURL = imgURL;
        this.uid = uid;
    }

    public User() {
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

    protected User(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.imgURL = in.readString();
        this.uid = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
