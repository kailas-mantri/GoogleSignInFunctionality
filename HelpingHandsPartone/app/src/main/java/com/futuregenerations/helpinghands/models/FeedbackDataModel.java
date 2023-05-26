package com.futuregenerations.helpinghands.models;

public class FeedbackDataModel {
    String userFeedback, date, time;

    public FeedbackDataModel(String userFeedback, String date, String time) {
        this.userFeedback = userFeedback;
        this.date = date;
        this.time = time;
    }

    public FeedbackDataModel() {
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
