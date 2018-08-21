package com.ahlikasir.aplikasi.kasironline.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Firmansyah on 22/05/2018.
 */

public class Login {
    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("emailuser")
    @Expose
    public String emailuser;
    @SerializedName("passworduser")
    @Expose
    public String passworduser;
    @SerializedName("emailparent")
    @Expose
    public String emailparent;

    public Login(String emailuser, String passworduser) {
        this.emailuser = emailuser;
        this.passworduser = passworduser;
    }

    public String getEmailparent() {
        return emailparent;
    }

    public void setEmailparent(String emailparent) {
        this.emailparent = emailparent;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getPassworduser() {
        return passworduser;
    }

    public void setPassworduser(String passworduser) {
        this.passworduser = passworduser;
    }
}
