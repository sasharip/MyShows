package com.myshows.studentapp.model.eventbus;

public class EventBusMsg {

    protected Message message;

    protected EventBusMsg(Message message) {
        setMessage(message);
    }

    protected EventBusMsg() {
        this(null);
    }

    protected Message getMessage() {
        return message;
    }

    protected void setMessage(Message message) {
        this.message = message;
    }

}
