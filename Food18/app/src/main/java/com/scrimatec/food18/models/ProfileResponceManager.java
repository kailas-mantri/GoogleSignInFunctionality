package com.scrimatec.food18.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileResponceManager {

    @SerializedName("1")
    @Expose
    private String _1;
    @SerializedName("data")
    @Expose
    private List<ProfileResponce> data = null;

    public String get1() {
        return _1;
    }

    public void set1(String _1) {
        this._1 = _1;
    }

    public List<ProfileResponce> getData() {
        return data;
    }

    public void setData(List<ProfileResponce> data) {
        this.data = data;
    }

}
