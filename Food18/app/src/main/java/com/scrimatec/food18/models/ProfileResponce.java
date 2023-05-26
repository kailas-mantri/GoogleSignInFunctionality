package com.scrimatec.food18.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileResponce {

    @SerializedName("u_id")
    @Expose
    private String uId;

    @SerializedName("u_name")
    @Expose
    private String uName;

    @SerializedName("u_email")
    @Expose
    private String uEmail;

    @SerializedName("u_dob")
    @Expose
    private String uDob;

    @SerializedName("mob")
    @Expose
    private String mob;

    @SerializedName("u_gen")
    @Expose
    private String uGen;

    public String getUId() { return uId; }

    public void setUId(String uId) { this.uId = uId; }

    public String getUName() { return uName; }

    public void setUName(String uName) { this.uName = uName; }

    public String getUEmail() { return uEmail; }

    public void setUEmail(String uEmail) { this.uEmail = uEmail; }

    public String getUDob() { return uDob; }

    public void setUDob(String uDob) { this.uDob = uDob; }

    public String getMob() { return mob; }

    public void setMob(String mob) { this.mob = mob; }

    public String getUGen() { return uGen; }

    public void setUGen(String uGen) { this.uGen = uGen; }

}