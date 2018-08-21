package com.ahlikasir.aplikasi.kasironline.model.kelompok;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Firmansyah on 09/05/2018.
 */

public class Kelompok {
    @SerializedName("idkelompok")
    @Expose
    private String idkelompok;
    @SerializedName("kelompok")
    @Expose
    private String kelompok;
    @SerializedName("emailkelompok")
    @Expose
    private String emailkelompok;
    @SerializedName("status")
    @Expose
    private String status;

    public Kelompok(String kelompok,String emailkelompok){
        this.kelompok = kelompok;
        this.emailkelompok = emailkelompok;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdkelompok() {
        return idkelompok;
    }

    public void setIdkelompok(String idkelompok) {
        this.idkelompok = idkelompok;
    }

    public String getKelompok() {
        return kelompok;
    }

    public void setKelompok(String kelompok) {
        this.kelompok = kelompok;
    }

    public String getEmailkelompok() {
        return emailkelompok;
    }

    public void setEmailkelompok(String emailkelompok) {
        this.emailkelompok = emailkelompok;
    }
}
