package com.futuregenerations.helpinghands.models;

 public class PaymentDetailsModel {
     String transactionID, organizationID, organizationTitle, donationDate, donationTime, donationAmount, userID, orderID;

     public PaymentDetailsModel(String transactionID, String organizationID, String organizationTitle, String donationDate, String donationTime, String donationAmount, String userID, String orderID) {
         this.transactionID = transactionID;
         this.organizationID = organizationID;
         this.organizationTitle = organizationTitle;
         this.donationDate = donationDate;
         this.donationTime = donationTime;
         this.donationAmount = donationAmount;
         this.userID = userID;
         this.orderID = orderID;
     }

     public PaymentDetailsModel() {
     }

     public String getTransactionID() {
         return transactionID;
     }

     public void setTransactionID(String transactionID) {
         this.transactionID = transactionID;
     }

     public String getOrganizationID() {
         return organizationID;
     }

     public void setOrganizationID(String organizationID) {
         this.organizationID = organizationID;
     }

     public String getOrganizationTitle() {
         return organizationTitle;
     }

     public void setOrganizationTitle(String organizationTitle) {
         this.organizationTitle = organizationTitle;
     }

     public String getDonationDate() {
         return donationDate;
     }

     public void setDonationDate(String donationDate) {
         this.donationDate = donationDate;
     }

     public String getDonationTime() {
         return donationTime;
     }

     public void setDonationTime(String donationTime) {
         this.donationTime = donationTime;
     }

     public String getDonationAmount() {
         return donationAmount;
     }

     public void setDonationAmount(String donationAmount) {
         this.donationAmount = donationAmount;
     }

     public String getUserID() {
         return userID;
     }

     public void setUserID(String userID) {
         this.userID = userID;
     }

     public String getOrderID() {
         return orderID;
     }

     public void setOrderID(String orderID) {
         this.orderID = orderID;
     }
 }
