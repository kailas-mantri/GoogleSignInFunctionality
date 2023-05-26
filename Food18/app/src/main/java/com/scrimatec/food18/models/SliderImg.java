package com.scrimatec.food18.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderImg {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("slid_desc")
    @Expose
    private String slidDesc;
    @SerializedName("slid_img")
    @Expose
    private String slidImg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlidDesc() { return slidDesc; }

    public void setSlidDesc(String slidDesc) {
        this.slidDesc = slidDesc;
    }

    public String getSlidImg() {
        return slidImg;
    }

    public void setSlidImg(String slidImg) {
        this.slidImg = slidImg;
    }

}