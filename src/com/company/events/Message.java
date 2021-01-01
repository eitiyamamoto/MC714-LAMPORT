package com.company.events;

import java.io.Serializable;

public class Message implements Serializable {

    private int time;
    private String message;

    public Message() {
    }

    public Message(int time, String message) {
        this.time = time;
        this.message = message;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
