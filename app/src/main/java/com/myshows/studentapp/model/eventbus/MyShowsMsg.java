package com.myshows.studentapp.model.eventbus;

import com.myshows.studentapp.model.Show;

import java.util.ArrayList;

public class MyShowsMsg extends EventBusMsg {

    private ArrayList<Show> shows;

    public MyShowsMsg(Message message) {
        super(message);
    }

    public MyShowsMsg() {}

    @Override
    public Message getMessage() {
        return super.getMessage();
    }

    @Override
    public void setMessage(Message message) {
        super.setMessage(message);
    }

    public ArrayList<Show> getShows() {
        return shows;
    }

    public void setShows(ArrayList<Show> shows) {
        this.shows = shows;
    }
}
