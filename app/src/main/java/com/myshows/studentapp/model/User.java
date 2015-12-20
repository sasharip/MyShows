package com.myshows.studentapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName("login")
    @Expose
    public String login;

    @SerializedName("avatar")
    @Expose
    public String avatarUrl;

    @SerializedName("wastedTime")
    @Expose
    public String wastedTime;

    @SerializedName("gender")
    @Expose
    public String gender;

    @SerializedName("stats")
    @Expose
    public UserStats stats;

    public User(Parcel in) {
        login = in.readString();
        avatarUrl = in.readString();
        wastedTime = in.readString();
        gender = in.readString();
        stats = in.readParcelable(UserStats.class.getClassLoader());
    }

    public User() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeString(avatarUrl);
        dest.writeString(wastedTime);
        dest.writeString(gender);
        dest.writeParcelable(stats, flags);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
