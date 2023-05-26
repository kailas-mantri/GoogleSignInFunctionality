package com.scrimatec.food18.models;

public class SignUpResponse {
    String st,id;

    public SignUpResponse(String st, String id) {
        this.st = st;
        this.id = id;
    }

    public SignUpResponse() {
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
