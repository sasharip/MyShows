package com.myshows.studentapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserStats implements Parcelable {

    @SerializedName("watchedHours")
    @Expose
    public String watchedHours;

    @SerializedName("remainingHours")
    @Expose
    public String remainingHours;

    @SerializedName("watchedEpisodes")
    @Expose
    public String watchedEpisodes;

    @SerializedName("remainingEpisodes")
    @Expose
    public String remainingEpisodes;

    @SerializedName("totalEpisodes")
    @Expose
    public String totalEpisodes;

    @SerializedName("totalDays")
    @Expose
    public String totalDays;

    @SerializedName("totalHours")
    @Expose
    public String totalHours;

    @SerializedName("remainingDays")
    @Expose
    public String remainingDays;

    @SerializedName("watchedDays")
    @Expose
    public String watchedDays;

    public UserStats(Parcel in){
        watchedHours = in.readString();
        remainingHours = in.readString();
        watchedEpisodes = in.readString();
        remainingEpisodes = in.readString();
        totalEpisodes = in.readString();
        totalDays = in.readString();
        totalHours = in.readString();
        remainingDays = in.readString();
        watchedDays = in.readString();
    }

    public UserStats(){}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(watchedHours);
        dest.writeString(remainingHours);
        dest.writeString(watchedEpisodes);
        dest.writeString(remainingEpisodes);
        dest.writeString(totalEpisodes);
        dest.writeString(totalDays);
        dest.writeString(totalHours);
        dest.writeString(remainingDays);
        dest.writeString(watchedDays);
    }

    public static final Creator<UserStats> CREATOR = new Creator<UserStats>() {
        @Override
        public UserStats createFromParcel(Parcel in) {
            return new UserStats(in);
        }

        @Override
        public UserStats[] newArray(int size) {
            return new UserStats[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
