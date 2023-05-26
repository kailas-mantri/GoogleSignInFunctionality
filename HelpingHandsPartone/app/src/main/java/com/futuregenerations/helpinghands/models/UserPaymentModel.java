package com.futuregenerations.helpinghands.models;

public class UserPaymentModel {
    UserDataModel userDetails;
    GetOrganizationsDataModel organizationDetails;
    PaymentDetailsModel paymentDetails;

    public UserPaymentModel(UserDataModel userDetails, GetOrganizationsDataModel organizationDetails, PaymentDetailsModel paymentDetails) {
        this.userDetails = userDetails;
        this.organizationDetails = organizationDetails;
        this.paymentDetails = paymentDetails;
    }

    public UserPaymentModel() {
    }

    public UserDataModel getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDataModel userDetails) {
        this.userDetails = userDetails;
    }

    public GetOrganizationsDataModel getOrganizationDetails() {
        return organizationDetails;
    }

    public void setOrganizationDetails(GetOrganizationsDataModel organizationDetails) {
        this.organizationDetails = organizationDetails;
    }

    public PaymentDetailsModel getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetailsModel paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}
