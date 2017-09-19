package com.example.beavi5.vktest.MODEL.models;

/**
 * Created by beavi5 on 19.09.2017.
 */

public class UploadedPhoto {
    String server;
    String photo;
    String hash;

    public String getServer() {
        return server;
    }

    public String getPhoto() {
        return photo;
    }

    public String getHash() {
        return hash;
    }

    public UploadedPhoto(String server, String photo, String hash) {

        this.server = server;
        this.photo = photo;
        this.hash = hash;
    }
}
