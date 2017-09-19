package com.example.beavi5.vktest.MODEL.models;

/**
 * Created by beavi5 on 18.09.2017.
 */

public class FriendModel {
    String photoUrl;
    String firstName;
    String lastName;
    String userId;

    public FriendModel(String photoUrl, String firstName, String lastName, String userId) {
        this.photoUrl = photoUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUserId() {
        return userId;
    }

    public String getLastName() {

        return lastName;
    }

}
