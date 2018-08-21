package com.ahlikasir.aplikasi.kasironline.Activity.transaksi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.Activity.transaksi.pembayaran.PembayaranListActivity
import com.ahlikasir.aplikasi.kasironline.Activity.transaksi.penjualan.PenjualanActivity
import com.ahlikasir.aplikasi.kasironline.Activity.transaksi.user.UserListActivity
import kotlinx.android.synthetic.main.activity_transaksi.*

class TransaksiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi)

        this.title = "Menu Transaksi"

        val emailCek = Function().getShared("emailCek","",this)

        if(emailCek == "false"){
            cardView12.visibility = View.GONE
        }

        cardView10.setOnClickListener {
            val intent = Intent(this,PenjualanActivity::class.java)
            Function().setSharedPrefrences("pelanggan","kosong",this@TransaksiActivity)
            Function().setSharedPrefrences("idpelanggan","kosong",this@TransaksiActivity)
            Function().setSharedPrefrences("barang","kosong",this@TransaksiActivity)
            Function().setSharedPrefrences("idbarang","kosong",this@TransaksiActivity)
            startActivity(intent)
        }

        cardView11.setOnClickListener {
            val intent = Intent(this,PembayaranListActivity::class.java)
            startActivity(intent)
        }

        cardView12.setOnClickListener {
            val intent = Intent(this,UserListActivity::class.java)
            startActivity(intent)
        }

    }
}
