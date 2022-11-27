package com.example.ihc.data;

import java.util.ArrayList;

public class User {


    private String uuid;
    private String name, email, photoUri, country;
    private ArrayList<String> matchesUUIDs=new ArrayList<>();

    public User() {
        matchesUUIDs=new ArrayList<>();
    }

    public User(String uuid, String name, String email, String photoUri, String country, ArrayList<String> matchesUUIDs) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.photoUri = photoUri;
        this.country = country;
        this.matchesUUIDs = matchesUUIDs;
    }

    public String getUuid() {
        return uuid;
    }

    public ArrayList<String> getMatchesUUIDs() {
        return matchesUUIDs;
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String phoneNo, String country) {
        this.name = name;
        this.country = country;
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
}
