package com.ahlikasir.aplikasi.kasironline.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Firmansyah on 23/05/2018.
 */

public class Register {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("emailuser")
    @Expose
    public String emailuser;
    @SerializedName("notelp")
    @Expose
    public String notelp;
    @SerializedName("passworduser")
    @Expose
    public String passworduser;

    public Register(String emailuser, String notelp, String passworduser) {
        this.emailuser = emailuser;
        this.notelp = notelp;
        this.passworduser = passworduser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmailuser() {
        return emailuser;
    }

    public void setEmailuser(String emailuser) {
        this.emailuser = emailuser;
    }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }

    public String getPassworduser() {
        return passworduser;
    }

    public void setPassworduser(String passworduser) {
        this.passworduser = passworduser;
    }
}
