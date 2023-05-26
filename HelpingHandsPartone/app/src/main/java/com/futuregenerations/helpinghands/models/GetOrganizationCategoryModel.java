package com.futuregenerations.helpinghands.models;

public class GetOrganizationCategoryModel {
    String categoryID, categoryTitle, categoryDescription, categoryImageURL,categoryImageName;

    public GetOrganizationCategoryModel(String categoryID, String categoryTitle, String categoryDescription, String categoryImageURL, String categoryImageName) {
        this.categoryID = categoryID;
        this.categoryTitle = categoryTitle;
        this.categoryDescription = categoryDescription;
        this.categoryImageURL = categoryImageURL;
        this.categoryImageName = categoryImageName;
    }

    public GetOrganizationCategoryModel() {
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

    public String getCategoryImageName() {
        return categoryImageName;
    }

    public void setCategoryImageName(String categoryImageName) {
        this.categoryImageName = categoryImageName;
    }
}
