package com.myshows.studentapp.model.eventbus;

public class LoginMsg extends EventBusMsg {

    public boolean isSocial;

    public LoginMsg(Message message, boolean isSocial) {
        super(message);
        this.isSocial = isSocial;
    }

    public LoginMsg(boolean isSocial) {
        this.isSocial = isSocial;
    }

    @Override
    public Message getMessage() {
        return super.getMessage();
    }

    @Override
    public void setMessage(Message message) {
        super.setMessage(message);
    }

}
