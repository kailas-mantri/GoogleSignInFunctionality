package com.futuregenerations.helpinghands.models;

public class UserSharedPrefData {
    String themeName, notificationStatus;

    public UserSharedPrefData(String themeName, String notificationStatus) {
        this.themeName = themeName;
        this.notificationStatus = notificationStatus;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
}
