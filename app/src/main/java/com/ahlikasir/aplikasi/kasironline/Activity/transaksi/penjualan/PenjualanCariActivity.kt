package com.ahlikasir.aplikasi.kasironline.Activity.transaksi.penjualan

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.adapter.penjualan.PenjualanCariBarAdapter
import com.ahlikasir.aplikasi.kasironline.adapter.penjualan.PenjualanCariPelAdapter
import com.ahlikasir.aplikasi.kasironline.model.barang.Barang
import com.ahlikasir.aplikasi.kasironline.model.pelanggan.Pelanggan
import com.ahlikasir.aplikasi.kasironline.model.transaksi.PenjualanPelanggan
import kotlinx.android.synthetic.main.activity_penjualan_cari.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class PenjualanCariActivity : AppCompatActivity() {

    lateinit var pelAdapter:PenjualanCariPelAdapter
    lateinit var barAdapter:PenjualanCariBarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penjualan_cari)

        if (type()){
            loadDataPelanggan()
        }else{
            loadDataBarang()
        }

    }

    fun type():Boolean{
        val type = intent.getStringExtra("cariType")

        return type == "pelanggan"

    }

    private fun loadDataPelanggan() {
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        request.getPelanggan(encodeEmail,token).enqueue(object: retrofit2.Callback<List<Pelanggan>> {
            override fun onResponse(call: Call<List<Pelanggan>>?, response: Response<List<Pelanggan>>?) {
                if (response!!.isSuccessful){
                    pelAdapter = PenjualanCariPelAdapter(this@PenjualanCariActivity,response.body(),{pelanggan ->
                        val faktur = Function().getShared("fakturLama","",this@PenjualanCariActivity)
                        val isiUpdate = PenjualanPelanggan(pelanggan.idpelanggan,faktur,email)
                        request.updateIdPelanggan(isiUpdate,token).enqueue(object: Callback<PenjualanPelanggan>{
                            override fun onFailure(call: Call<PenjualanPelanggan>?, t: Throwable?) {
                                Function().toast("Something Went Wrong Try Again",this@PenjualanCariActivity)
                            }

                            override fun onResponse(call: Call<PenjualanPelanggan>?, response: Response<PenjualanPelanggan>?) {

                                if(response!!.isSuccessful){
                                    if(response.body().status == "true"){
                                        Function().setSharedPrefrences("pelanggan",pelanggan.pelanggan,this@PenjualanCariActivity)
                                        Function().setSharedPrefrences("idpelanggan",pelanggan.idpelanggan,this@PenjualanCariActivity)
                                        finish()
                                    }else{
                                        Function().setSharedPrefrences("pelanggan",pelanggan.pelanggan,this@PenjualanCariActivity)
                                        Function().setSharedPrefrences("idpelanggan",pelanggan.idpelanggan,this@PenjualanCariActivity)
                                        finish()
                                    }
                                }


                            }
                        })

                    })
                    recCari.adapter = pelAdapter
                    val layoutmanager = LinearLayoutManager(this@PenjualanCariActivity)
                    recCari.layoutManager = layoutmanager
                    recCari.setHasFixedSize(true)
                }else{
                    Toast.makeText(this@PenjualanCariActivity,"Tidak Ada Data", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Pelanggan>>?, t: Throwable?) {
                val alert = AlertDialog.Builder(this@PenjualanCariActivity)
                alert.setTitle("ERROR")
                alert.setMessage("terjadi error, coba lagi")
                alert.setPositiveButton("RETRY",{dialog, which ->
                    loadDataPelanggan()
                    dialog.dismiss()
                })
                alert.show()
            }
        })
    }

    private fun loadDataBarang() {
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        request.getBarang(encodeEmail,token).enqueue(object : retrofit2.Callback<List<Barang>>{
            override fun onResponse(call: Call<List<Barang>>?, response: Response<List<Barang>>?) {
                if (response!!.isSuccessful){
                    barAdapter = PenjualanCariBarAdapter(this@PenjualanCariActivity,response.body(),{barang ->
                        Function().setSharedPrefrences("barang",barang.barang,this@PenjualanCariActivity)
                        Function().setSharedPrefrences("idbarang",barang.idbarang,this@PenjualanCariActivity)
                        Function().setSharedPrefrences("stok",barang.stok,this@PenjualanCariActivity)
                        finish()
                    })
                    recCari.adapter = barAdapter
                    val layoutmanager = LinearLayoutManager(this@PenjualanCariActivity)
                    recCari.layoutManager = layoutmanager
                    recCari.setHasFixedSize(true)
                }else{
                    Toast.makeText(this@PenjualanCariActivity,"Tidak Ada data",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Barang>>?, t: Throwable?) {
                val alert = AlertDialog.Builder(this@PenjualanCariActivity)
                alert.setTitle("ERROR")
                alert.setMessage("terjadi error, coba lagi")
                alert.setPositiveButton("RETRY",{dialog, which ->
                    loadDataBarang()
                    dialog.dismiss()
                })
                alert.show()
            }
        })
    }
}
