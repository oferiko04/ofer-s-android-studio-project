package com.example.finalproject;

public class Message {
    private String fromSender;
    private String comp ;

    public Message() {
    }

    public Message(String fromSender, String comp) {
        this.fromSender = fromSender;
        this.comp = comp;
    }

    public void setFromSender(String fromSender) {
        this.fromSender = fromSender;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getFromSender() {
        return fromSender;
    }

    public String getComp() {
        return comp;
    }
}
