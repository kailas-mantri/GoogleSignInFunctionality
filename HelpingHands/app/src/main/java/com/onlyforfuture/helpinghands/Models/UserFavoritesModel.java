package com.onlyforfuture.helpinghands.Models;

public class UserFavoritesModel {

    String getOrganizationID, getOrganizationType, getOrganizationTitle, getOrganizationLogo, getOrganizationLocation, getOrganizationDescription, getOrganizationCategory;


    public UserFavoritesModel(String organizationID, String organizationType, String organizationTitle, String organizationLogo,
                              String organizationLocation, String organizationDescription, String organizationCategory) {
        this.getOrganizationID = organizationID;
        this.getOrganizationType  = organizationType;
        this.getOrganizationTitle = organizationTitle;
        this.getOrganizationLogo = organizationLogo;
        this.getOrganizationLocation = organizationLocation;
        this.getOrganizationDescription = organizationDescription;
        this.getOrganizationCategory = organizationCategory;
    }

    public String getGetOrganizationID() {
        return getOrganizationID;
    }

    public void setGetOrganizationID(String getOrganizationID) {
        this.getOrganizationID = getOrganizationID;
    }

    public String getGetOrganizationType() {
        return getOrganizationType;
    }

    public void setGetOrganizationType(String getOrganizationType) {
        this.getOrganizationType = getOrganizationType;
    }

    public String getGetOrganizationTitle() {
        return getOrganizationTitle;
    }

    public void setGetOrganizationTitle(String getOrganizationTitle) {
        this.getOrganizationTitle = getOrganizationTitle;
    }

    public String getGetOrganizationLogo() {
        return getOrganizationLogo;
    }

    public void setGetOrganizationLogo(String getOrganizationLogo) {
        this.getOrganizationLogo = getOrganizationLogo;
    }

    public String getGetOrganizationLocation() {
        return getOrganizationLocation;
    }

    public void setGetOrganizationLocation(String getOrganizationLocation) {
        this.getOrganizationLocation = getOrganizationLocation;
    }

    public String getGetOrganizationDescription() {
        return getOrganizationDescription;
    }

    public void setGetOrganizationDescription(String getOrganizationDescription) {
        this.getOrganizationDescription = getOrganizationDescription;
    }

    public String getGetOrganizationCategory() {
        return getOrganizationCategory;
    }

    public void setGetOrganizationCategory(String getOrganizationCategory) {
        this.getOrganizationCategory = getOrganizationCategory;
    }
}
