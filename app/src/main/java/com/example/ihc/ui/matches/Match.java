package com.example.ihc.ui.matches;


public class Match {

    String name, mapId, lastMsgTime, phoneNo, country;
    String imageId;

    public Match(String name, String mapId, String lastMsgTime, String phoneNo, String country, String imageId) {
        this.name = name;
        this.mapId = mapId;
        this.lastMsgTime = lastMsgTime;
        this.phoneNo = phoneNo;
        this.country = country;
        this.imageId = imageId;
    }
}
