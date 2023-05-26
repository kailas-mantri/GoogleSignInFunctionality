package com.scrimatec.food18.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SliderImageModel {

    @SerializedName("slider_img")
    @Expose
    private List<SliderImg> sliderImg = null;

    public List<SliderImg> getSliderImg() {
        return sliderImg;
    }

    public void setSliderImg(List<SliderImg> sliderImg) {
        this.sliderImg = sliderImg;
    }

}
