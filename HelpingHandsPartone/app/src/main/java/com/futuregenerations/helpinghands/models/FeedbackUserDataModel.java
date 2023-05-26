package com.futuregenerations.helpinghands.models;

public class FeedbackUserDataModel {
    String userName, userEmail, userImage, userID;

    public FeedbackUserDataModel(String userName, String userEmail, String userImage, String userID) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userImage = userImage;
        this.userID = userID;
    }

    public FeedbackUserDataModel() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
