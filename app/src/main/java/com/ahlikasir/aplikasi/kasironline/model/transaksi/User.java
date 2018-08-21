package com.ahlikasir.aplikasi.kasironline.model.transaksi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Firmansyah on 11/06/2018.
 */

public class User {
    @SerializedName("iduser")
    @Expose
    public String iduser;
    @SerializedName("emailuser")
    @Expose
    public String emailuser;
    @SerializedName("notelp")
    @Expose
    public String notelp;
    @SerializedName("emailparent")
    @Expose
    public String emailparent;
    @SerializedName("passworduser")
    @Expose
    public String passworduser;
    @SerializedName("statususer")
    @Expose
    public String statususer;
    @SerializedName("lama")
    @Expose
    public String lama;
    @SerializedName("expired")
    @Expose
    public String expired;
    @SerializedName("status")
    @Expose
    public String status;

    public User(String emailuser, String notelp, String emailparent, String passworduser) {
        this.emailuser = emailuser;
        this.notelp = notelp;
        this.emailparent = emailparent;
        this.passworduser = passworduser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
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

    public String getEmailparent() {
        return emailparent;
    }

    public void setEmailparent(String emailparent) {
        this.emailparent = emailparent;
    }

    public String getPassworduser() {
        return passworduser;
    }

    public void setPassworduser(String passworduser) {
        this.passworduser = passworduser;
    }

    public String getStatususer() {
        return statususer;
    }

    public void setStatususer(String statususer) {
        this.statususer = statususer;
    }

    public String getLama() {
        return lama;
    }

    public void setLama(String lama) {
        this.lama = lama;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }
}
