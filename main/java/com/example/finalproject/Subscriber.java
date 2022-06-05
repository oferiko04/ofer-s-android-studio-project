package com.example.finalproject;

public class Subscriber {
    private String subName;
    private String subPassword;

    public Subscriber() {
    }

    public Subscriber(String subName, String subPassword) {
        this.subName = subName;
        this.subPassword = subPassword;
    }

    public String getSubName() {
        return subName;
    }

    public String getSubPassword() {
        return subPassword;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public void setSubPassword(String subPassword) {
        this.subPassword = subPassword;
    }

}
