package com.ahlikasir.aplikasi.kasironline.model.toko;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Firmansyah on 16/05/2018.
 */

public class TokoUpIn {
    @SerializedName("idtoko")
    @Expose
    public String idtoko;
    @SerializedName("toko")
    @Expose
    public String toko;
    @SerializedName("alamat")
    @Expose
    public String alamat;
    @SerializedName("telp")
    @Expose
    public String telp;
    @SerializedName("promo1")
    @Expose
    public String promo1;
    @SerializedName("promo2")
    @Expose
    public String promo2;
    @SerializedName("emailtoko")
    @Expose
    public String emailtoko;
    @SerializedName("maxemail")
    @Expose
    public String maxemail;
    @SerializedName("maxdata")
    @Expose
    public String maxdata;
    @SerializedName("status")
    @Expose
    public String status;

    public TokoUpIn(String toko, String alamat, String telp, String promo1, String promo2, String emailtoko) {
        this.toko = toko;
        this.alamat = alamat;
        this.telp = telp;
        this.promo1 = promo1;
        this.promo2 = promo2;
        this.emailtoko = emailtoko;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdtoko() {
        return idtoko;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getPromo1() {
        return promo1;
    }

    public void setPromo1(String promo1) {
        this.promo1 = promo1;
    }

    public String getPromo2() {
        return promo2;
    }

    public void setPromo2(String promo2) {
        this.promo2 = promo2;
    }

    public String getEmailtoko() {
        return emailtoko;
    }

    public void setEmailtoko(String emailtoko) {
        this.emailtoko = emailtoko;
    }

    public String getMaxemail() {
        return maxemail;
    }

    public void setMaxemail(String maxemail) {
        this.maxemail = maxemail;
    }

    public String getMaxdata() {
        return maxdata;
    }

    public void setMaxdata(String maxdata) {
        this.maxdata = maxdata;
    }
}
