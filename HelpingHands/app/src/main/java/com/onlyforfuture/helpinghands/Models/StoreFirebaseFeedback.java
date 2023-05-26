package com.onlyforfuture.helpinghands.Models;

public class StoreFirebaseFeedback {

    String userFeedback, date, time;

    public StoreFirebaseFeedback(String userFeedback, String date, String time) {
        this.userFeedback = userFeedback;
        this.date = date;
        this.time = time;
    }

    public StoreFirebaseFeedback() {
    }

    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
