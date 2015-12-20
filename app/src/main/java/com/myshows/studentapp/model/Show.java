package com.myshows.studentapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Show implements Parcelable{

    public String id;
    public String title;
    public int totalEpisodes;
    public int watchedEpisodes;
    public String image;

    protected Show(Parcel in) {
        id = in.readString();
        title = in.readString();
        totalEpisodes = in.readInt();
        watchedEpisodes = in.readInt();
        image = in.readString();
    }

    public Show() {}

    public static final Creator<Show> CREATOR = new Creator<Show>() {
        @Override
        public Show createFromParcel(Parcel in) {
            return new Show(in);
        }

        @Override
        public Show[] newArray(int size) {
            return new Show[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeInt(totalEpisodes);
        dest.writeInt(watchedEpisodes);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
