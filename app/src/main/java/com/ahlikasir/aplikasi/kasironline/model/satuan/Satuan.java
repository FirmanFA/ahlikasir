package com.ahlikasir.aplikasi.kasironline.model.satuan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Firmansyah on 15/05/2018.
 */

public class Satuan {
    @SerializedName("idsatuan")
    @Expose
    private String idsatuan;
    @SerializedName("satuan1")
    @Expose
    private String satuan1;
    @SerializedName("nilai1")
    @Expose
    private String nilai1;
    @SerializedName("satuan2")
    @Expose
    private String satuan2;
    @SerializedName("nilai2")
    @Expose
    private String nilai2;
    @SerializedName("emailsatuan")
    @Expose
    private String emailsatuan;
    @SerializedName("status")
    @Expose
    private String status;

    public Satuan(String satuan1, String nilai1, String satuan2, String nilai2,String emailsatuan) {
        this.satuan1 = satuan1;
        this.nilai1 = nilai1;
        this.satuan2 = satuan2;
        this.nilai2 = nilai2;
        this.emailsatuan = emailsatuan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdsatuan() {
        return idsatuan;
    }

    public void setIdsatuan(String idsatuan) {
        this.idsatuan = idsatuan;
    }

    public String getSatuan1() {
        return satuan1;
    }

    public void setSatuan1(String satuan1) {
        this.satuan1 = satuan1;
    }

    public String getNilai1() {
        return nilai1;
    }

    public void setNilai1(String nilai1) {
        this.nilai1 = nilai1;
    }

    public String getSatuan2() {
        return satuan2;
    }

    public void setSatuan2(String satuan2) {
        this.satuan2 = satuan2;
    }

    public String getNilai2() {
        return nilai2;
    }

    public void setNilai2(String nilai2) {
        this.nilai2 = nilai2;
    }

    public String getEmailsatuan() {
        return emailsatuan;
    }

    public void setEmailsatuan(String emailsatuan) {
        this.emailsatuan = emailsatuan;
    }
}
