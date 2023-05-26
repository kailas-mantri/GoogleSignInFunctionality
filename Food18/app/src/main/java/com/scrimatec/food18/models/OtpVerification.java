package com.scrimatec.food18.models;

public class OtpVerification {
    String otp,st,msg;

    public OtpVerification(String otp, String st, String msg) {
        this.otp = otp;
        this.st = st;
        this.msg = msg;
    }

    public OtpVerification() { }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "OtpVerification{" +
                "st='" + st + '\'' +
                ", otp='" + otp + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
