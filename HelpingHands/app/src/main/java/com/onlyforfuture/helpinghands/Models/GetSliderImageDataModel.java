package com.onlyforfuture.helpinghands.Models;

public class GetSliderImageDataModel {
    String imageId, imageURL, description;

    public GetSliderImageDataModel(String imageId, String imageURL, String description) {
        this.imageId = imageId;
        this.imageURL = imageURL;
        this.description = description;
    }

    public GetSliderImageDataModel() {
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
