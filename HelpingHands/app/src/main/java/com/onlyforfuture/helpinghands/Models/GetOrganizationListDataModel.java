package com.onlyforfuture.helpinghands.Models;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class GetOrganizationListDataModel {
    String organizationID, organizationDescription, organizationImageURL, organizationTitle, organizationType, organizationLocation;
    List<String> organizationImages;

    String organizationCategory;

    public GetOrganizationListDataModel(String organizationID, String organizationDescription, String organizationImageURL, String organizationTitle, String organizationType, String organizationLocation, String organizationCategory, List<String> organizationImages ) {
        this.organizationID = organizationID;
        this.organizationImageURL = organizationImageURL;
        this.organizationTitle = organizationTitle;
        this.organizationType = organizationType;
        this.organizationDescription = organizationDescription;
        this.organizationCategory = organizationCategory;
        this.organizationLocation = organizationLocation;
        this.organizationImages = organizationImages;
    }

    public GetOrganizationListDataModel() {

    }

    public String getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    public String getOrganizationImageURL() {
        return organizationImageURL;
    }

    public void setOrganizationImageURL(String organizationImageURL) {
        this.organizationImageURL = organizationImageURL;
    }

    public String getOrganizationTitle() {
        return organizationTitle;
    }

    public void setOrganizationTitle(String organizationTitle) {
        this.organizationTitle = organizationTitle;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrganizationDescription() {
        return organizationDescription;
    }

    public void setOrganizationDescription(String organizationDescription) {
        this.organizationDescription = organizationDescription;
    }

    public String getOrganizationCategory() {return organizationCategory; }

    public void setOrganizationCategory(String organizationCategory) {
        this.organizationCategory = organizationCategory;
    }

    public String getOrganizationLocation() {
        return organizationLocation;
    }

    public void setOrganizationLocation(String organizationLocation) {
        this.organizationLocation = organizationLocation;
    }

    public List<String> getOrganizationImages() {
        return organizationImages;
    }

    public void setOrganizationImages(List<String> organizationImages) {
        this.organizationImages = organizationImages;
    }
}
