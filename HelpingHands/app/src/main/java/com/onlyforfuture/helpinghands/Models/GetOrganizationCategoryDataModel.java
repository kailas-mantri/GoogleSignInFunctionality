package com.onlyforfuture.helpinghands.Models;

public class GetOrganizationCategoryDataModel {
    String categoryID, categoryTitle, categoryDescription, categoryImageURL;

    public GetOrganizationCategoryDataModel(String categoryID, String categoryTitle, String categoryDescription, String categoryImageURL) {
        this.categoryID = categoryID;
        this.categoryTitle = categoryTitle;
        this.categoryDescription = categoryDescription;
        this.categoryImageURL = categoryImageURL;
    }

    public GetOrganizationCategoryDataModel() {
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryImageURL() {
        return categoryImageURL;
    }

    public void setCategoryImageURL(String categoryImageURL) {
        this.categoryImageURL = categoryImageURL;
    }
}
