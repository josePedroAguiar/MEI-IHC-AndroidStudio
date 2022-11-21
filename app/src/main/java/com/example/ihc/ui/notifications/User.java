package com.example.ihc.ui.notifications;


public class User {

    String name, lastMessage, lastMsgTime, phoneNo, country;
    int imageId;

    public User() {

    }

    public User(String name, String lastMessage, String lastMsgTime, String phoneNo, String country, int imageId) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.lastMsgTime = lastMsgTime;
        this.phoneNo = phoneNo;
        this.country = country;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
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
}
