package com.myshows.studentapp.model.eventbus;

import com.myshows.studentapp.model.User;

public class UserProfileMsg extends EventBusMsg {

    private User user;

    public UserProfileMsg(Message message) {
        super(message);
    }

    public UserProfileMsg() {}

    @Override
    public Message getMessage() {
        return super.getMessage();
    }

    @Override
    public void setMessage(Message message) {
        super.setMessage(message);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
