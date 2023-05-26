package com.scrimatec.fb_authentication;

public class User {
    String uid,str,str1;
    public User(String uid, String str, String str1) {
        this.str=str;
        this.str1=str1;
        this.uid=uid; }

    public User(int i, String str, String strl) { }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getStr() { return str; }

    public void setStr(String str) { this.str = str; }

    public String getStr1() { return str1; }

    public void setStr1(String str1) { this.str1 = str1; }
}


