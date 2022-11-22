package com.example.ihc.ui.notifications;


public class User {
    public User() {
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getCountry() {
        return country;
    }

    public int getImageId() {
        return imageId;
    }

    String name, lastMessage, lastMsgTime, phoneNo, country;
    int imageId;

    public User(String name, String lastMessage, String lastMsgTime, String phoneNo, String country, int imageId) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.lastMsgTime = lastMsgTime;
        this.phoneNo = phoneNo;
        this.country = country;
        this.imageId = imageId;
    }
}
