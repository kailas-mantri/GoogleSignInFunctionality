package com.onlyforfuture.helpinghands.Models;

public class StoreFirebaseUserFeedback {
    String userName, userEmail, userID, userImage;

    public StoreFirebaseUserFeedback(String userName, String userEmail, String userID, String userImage) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userID = userID;
        this.userImage = userImage;
    }

    public StoreFirebaseUserFeedback() {
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
