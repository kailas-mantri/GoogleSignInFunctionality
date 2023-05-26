package com.futuregenerations.helpinghands.models;

public class UserFavoritesModel {
    String organizationID, organizationType, organizationTitle, organizationImageURL, organizationLocation, organizationDescription, organizationCategory;

    public UserFavoritesModel() {
    }

    public UserFavoritesModel(String organizationID, String organizationType, String organizationTitle, String organizationImageURL, String organizationLocation, String organizationDescription, String organizationCategory) {
        this.organizationID = organizationID;
        this.organizationType = organizationType;
        this.organizationTitle = organizationTitle;
        this.organizationImageURL = organizationImageURL;
        this.organizationLocation = organizationLocation;
        this.organizationDescription = organizationDescription;
        this.organizationCategory = organizationCategory;
    }

    public String getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationTitle() {
        return organizationTitle;
    }

    public void setOrganizationTitle(String organizationTitle) {
        this.organizationTitle = organizationTitle;
    }

    public String getOrganizationImageURL() {
        return organizationImageURL;
    }

    public void setOrganizationImageURL(String organizationImageURL) {
        this.organizationImageURL = organizationImageURL;
    }

    public String getOrganizationLocation() {
        return organizationLocation;
    }

    public void setOrganizationLocation(String organizationLocation) {
        this.organizationLocation = organizationLocation;
    }

    public String getOrganizationDescription() {
        return organizationDescription;
    }

    public void setOrganizationDescription(String organizationDescription) {
        this.organizationDescription = organizationDescription;
    }

    public String getOrganizationCategory() {
        return organizationCategory;
    }

    public void setOrganizationCategory(String organizationCategory) {
        this.organizationCategory = organizationCategory;
    }
}
