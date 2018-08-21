package com.ahlikasir.aplikasi.kasironline.model.barang;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Firmansyah on 16/05/2018.
 */

public class Barang {
    @SerializedName("idbarang")
    @Expose
    public String idbarang;
    @SerializedName("idsatuan")
    @Expose
    public String idsatuan;
    @SerializedName("idkelompok")
    @Expose
    public String idkelompok;
    @SerializedName("barang")
    @Expose
    public String barang;
    @SerializedName("hargasatuan1")
    @Expose
    public String hargasatuan1;
    @SerializedName("hargasatuan2")
    @Expose
    public String hargasatuan2;
    @SerializedName("stok")
    @Expose
    public String stok;
    @SerializedName("emailbarang")
    @Expose
    public String emailbarang;
    @SerializedName("satuan1")
    @Expose
    public String satuan1;
    @SerializedName("nilai1")
    @Expose
    public String nilai1;
    @SerializedName("satuan2")
    @Expose
    public String satuan2;
    @SerializedName("nilai2")
    @Expose
    public String nilai2;
    @SerializedName("emailsatuan")
    @Expose
    public String emailsatuan;
    @SerializedName("kelompok")
    @Expose
    public String kelompok;
    @SerializedName("emailkelompok")
    @Expose
    public String emailkelompok;
    @SerializedName("status")
    @Expose
    public String status;

    public Barang(String satuan1, String kelompok, String barang, String hargasatuan1, String hargasatuan2, String stok, String emailbarang) {
        this.satuan1 = satuan1;
        this.kelompok = kelompok;
        this.barang = barang;
        this.hargasatuan1 = hargasatuan1;
        this.hargasatuan2 = hargasatuan2;
        this.stok = stok;
        this.emailbarang = emailbarang;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdbarang() {
        return idbarang;
    }

    public void setIdbarang(String idbarang) {
        this.idbarang = idbarang;
    }

    public String getIdsatuan() {
        return idsatuan;
    }

    public void setIdsatuan(String idsatuan) {
        this.idsatuan = idsatuan;
    }

    public String getIdkelompok() {
        return idkelompok;
    }

    public void setIdkelompok(String idkelompok) {
        this.idkelompok = idkelompok;
    }

    public String getBarang() {
        return barang;
    }

    public void setBarang(String barang) {
        this.barang = barang;
    }

    public String getHargasatuan1() {
        return hargasatuan1;
    }

    public void setHargasatuan1(String hargasatuan1) {
        this.hargasatuan1 = hargasatuan1;
    }

    public String getHargasatuan2() {
        return hargasatuan2;
    }

    public void setHargasatuan2(String hargasatuan2) {
        this.hargasatuan2 = hargasatuan2;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getEmailbarang() {
        return emailbarang;
    }

    public void setEmailbarang(String emailbarang) {
        this.emailbarang = emailbarang;
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
