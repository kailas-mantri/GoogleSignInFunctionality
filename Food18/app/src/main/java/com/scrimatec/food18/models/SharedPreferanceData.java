package com.scrimatec.food18.models;

public class SharedPreferanceData {
    String mobile_number, User_id;

    public SharedPreferanceData(String mobile_number, String user_id) {
        this.mobile_number = mobile_number;
        User_id = user_id;
    }

    public SharedPreferanceData() { }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

}
