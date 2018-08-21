package com.ahlikasir.aplikasi.kasironline.model.transaksi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Firmansyah on 26/06/2018.
 */

public class PenjualanPelanggan {
    @SerializedName("idpelanggan")
    @Expose
    public String idpelanggan;
    @SerializedName("faktur")
    @Expose
    public String faktur;
    @SerializedName("emailjual")
    @Expose
    public String emailjual;
    @SerializedName("status")
    @Expose
    public String status;

    public PenjualanPelanggan(String idpelanggan, String faktur,String emailjual) {
        this.idpelanggan = idpelanggan;
        this.faktur = faktur;
        this.emailjual = emailjual;
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

    public String getFaktur() {
        return faktur;
    }

    public void setFaktur(String faktur) {
        this.faktur = faktur;
    }

    public String getEmail() {
        return emailjual;
    }

    public void setEmail(String email) {
        this.emailjual = emailjual;
    }
}
