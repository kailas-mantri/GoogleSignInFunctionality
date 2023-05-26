package com.futuregenerations.helpinghands.models;

public class GetSliderDataModel {
    String description, imageURL, imageId;

    public GetSliderDataModel(String description, String imageURL, String imageId) {
        this.description = description;
        this.imageURL = imageURL;
        this.imageId = imageId;
    }

    public GetSliderDataModel() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
