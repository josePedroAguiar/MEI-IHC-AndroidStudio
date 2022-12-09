package com.example.ihc.data;

import java.util.ArrayList;

public class User {
    private String uuid;
    private int order;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;
    private int nMatches = 0;
    private String photoUri, bio, name, age, email, country = "";

    private ArrayList<String> matchesUiid = new ArrayList<>();

    public User() {
        matchesUiid = new ArrayList<>();
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUuid() {
        return uuid;
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getnMatches() {
        return nMatches;
    }

    public void setnMatches(int nMatches) {
        this.nMatches = nMatches;
    }
}
