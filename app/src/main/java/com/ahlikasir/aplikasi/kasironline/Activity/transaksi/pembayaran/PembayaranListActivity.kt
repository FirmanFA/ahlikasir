package com.ahlikasir.aplikasi.kasironline.Activity.transaksi.pembayaran

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.adapter.pembayaran.PembayaranAdapter
import com.ahlikasir.aplikasi.kasironline.model.transaksi.Pembayaran
import com.ahlikasir.aplikasi.kasironline.Activity.transaksi.penjualan.PenjualanActivity
import kotlinx.android.synthetic.main.activity_pembayaran_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class PembayaranListActivity : AppCompatActivity() {

    lateinit var adapter:PembayaranAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran_list)

        loadData()

    }

    private fun loadData() {
        val request = Function().builder()
        val token = Function().token(this)
        val emailraw = Function().email(this)
        val email = URLEncoder.encode(emailraw,"UTF-8")
        request.getPembayaran(email,token).enqueue(object : Callback<List<Pembayaran>>{
            override fun onResponse(call: Call<List<Pembayaran>>?, response: Response<List<Pembayaran>>?) {
                adapter = PembayaranAdapter(this@PembayaranListActivity,response!!.body(),{pembayaran ->
                    val intent = Intent(this@PembayaranListActivity,PenjualanActivity::class.java)
                    Function().setSharedPrefrences("fakturBayar",pembayaran.faktur,this@PembayaranListActivity)
                    Function().setSharedPrefrences("pelanggan",pembayaran.pelanggan,this@PembayaranListActivity)
                    Function().setSharedPrefrences("idpelanggan",pembayaran.idpelanggan,this@PembayaranListActivity)
                    Function().setSharedPrefrences("barang","kosong",this@PembayaranListActivity)
                    Function().setSharedPrefrences("idbarang","kosong",this@PembayaranListActivity)
                    startActivity(intent)
                })
                bayarRecycler.adapter = adapter
                val layoutmanager = LinearLayoutManager(this@PembayaranListActivity)
                bayarRecycler.layoutManager = layoutmanager
                bayarRecycler.setHasFixedSize(true)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<Pembayaran>>?, t: Throwable?) {
                val alert = AlertDialog.Builder(this@PembayaranListActivity)
                alert.setTitle("ERROR")
                alert.setMessage("terjadi error, coba lagi")
                alert.setPositiveButton("RETRY",{dialog, which ->
                    loadData()
                    dialog.dismiss()
                })
                alert.show()
            }
        })
    }

    override fun onResume() {
        loadData()
        super.onResume()
    }
}
