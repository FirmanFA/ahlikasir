package com.ahlikasir.aplikasi.kasironline.model.pelanggan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Firmansyah on 16/05/2018.
 */

public class Pelanggan {
    @SerializedName("idpelanggan")
    @Expose
    private String idpelanggan;
    @SerializedName("pelanggan")
    @Expose
    private String pelanggan;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("notelp")
    @Expose
    private String notelp;
    @SerializedName("emailpelanggan")
    @Expose
    private String emailpelanggan;
    @SerializedName("status")
    @Expose
    private String status;

    public Pelanggan(String pelanggan, String alamat, String notelp, String emailpelanggan) {
        this.pelanggan = pelanggan;
        this.alamat = alamat;
        this.notelp = notelp;
        this.emailpelanggan = emailpelanggan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdpelanggan() {
        return idpelanggan;
    }

    public void setIdpelanggan(String idpelanggan) {
        this.idpelanggan = idpelanggan;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(String pelanggan) {
        this.pelanggan = pelanggan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }

    public String getEmailpelanggan() {
        return emailpelanggan;
    }

    public void setEmailpelanggan(String emailpelanggan) {
        this.emailpelanggan = emailpelanggan;
    }
}
