package com.ahlikasir.aplikasi.kasironline.model.transaksi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Firmansyah on 30/05/2018.
 */

public class Penjualan {

    @SerializedName("iddetailjual")
    @Expose
    public String iddetailjual;
    @SerializedName("idbarang")
    @Expose
    public String idbarang;
    @SerializedName("idjual")
    @Expose
    public String idjual;
    @SerializedName("jumlah")
    @Expose
    public String jumlah;
    @SerializedName("hargajual")
    @Expose
    public String hargajual;
    @SerializedName("satuanjual")
    @Expose
    public String satuanjual;
    @SerializedName("keterangan")
    @Expose
    public String keterangan;
    @SerializedName("emaildetailjual")
    @Expose
    public String emaildetailjual;
    @SerializedName("idpelanggan")
    @Expose
    public String idpelanggan;
    @SerializedName("faktur")
    @Expose
    public String faktur;
    @SerializedName("tgljual")
    @Expose
    public String tgljual;
    @SerializedName("total")
    @Expose
    public String total;
    @SerializedName("bayar")
    @Expose
    public String bayar;
    @SerializedName("kembali")
    @Expose
    public String kembali;
    @SerializedName("emailjual")
    @Expose
    public String emailjual;
    @SerializedName("pelanggan")
    @Expose
    public String pelanggan;
    @SerializedName("alamat")
    @Expose
    public String alamat;
    @SerializedName("notelp")
    @Expose
    public String notelp;
    @SerializedName("emailpelanggan")
    @Expose
    public String emailpelanggan;
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
    @SerializedName("indexSatuan")
    @Expose
    public String indexSatuan;
    @SerializedName("pendapatan")
    @Expose
    public String pendapatan;
    @SerializedName("status")
    @Expose
    public String status;

    public Penjualan(String idbarang, String jumlah, String hargajual, String keterangan, String satuanjual, String idpelanggan, String faktur, String tgljual, String total, String bayar, String kembali, String emailjual,String indexSatuan) {
        this.idbarang = idbarang;
        this.jumlah = jumlah;
        this.hargajual = hargajual;
        this.keterangan = keterangan;
        this.satuanjual = satuanjual;
        this.idpelanggan = idpelanggan;
        this.faktur = faktur;
        this.tgljual = tgljual;
        this.total = total;
        this.bayar = bayar;
        this.kembali = kembali;
        this.emailjual = emailjual;
        this.indexSatuan = indexSatuan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIndexSatuan() {
        return indexSatuan;
    }

    public void setIndexSatuan(String indexSatuan) {
        this.indexSatuan = indexSatuan;
    }

    public String getPendapatan() {
        return pendapatan;
    }

    public void setPendapatan(String pendapatan) {
        this.pendapatan = pendapatan;
    }

    public String getIddetailjual() {
        return iddetailjual;
    }

    public void setIddetailjual(String iddetailjual) {
        this.iddetailjual = iddetailjual;
    }

    public String getIdbarang() {
        return idbarang;
    }

    public void setIdbarang(String idbarang) {
        this.idbarang = idbarang;
    }

    public String getIdjual() {
        return idjual;
    }

    public void setIdjual(String idjual) {
        this.idjual = idjual;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getHargajual() {
        return hargajual;
    }

    public void setHargajual(String hargajual) {
        this.hargajual = hargajual;
    }

    public String getSatuanjual() {
        return satuanjual;
    }

    public void setSatuanjual(String satuanjual) {
        this.satuanjual = satuanjual;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getEmaildetailjual() {
        return emaildetailjual;
    }

    public void setEmaildetailjual(String emaildetailjual) {
        this.emaildetailjual = emaildetailjual;
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

    public String getTgljual() {
        return tgljual;
    }

    public void setTgljual(String tgljual) {
        this.tgljual = tgljual;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBayar() {
        return bayar;
    }

    public void setBayar(String bayar) {
        this.bayar = bayar;
    }

    public String getKembali() {
        return kembali;
    }

    public void setKembali(String kembali) {
        this.kembali = kembali;
    }

    public String getEmailjual() {
        return emailjual;
    }

    public void setEmailjual(String emailjual) {
        this.emailjual = emailjual;
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
