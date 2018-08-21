package com.ahlikasir.aplikasi.kasironline.Activity.master

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ahlikasir.aplikasi.kasironline.Activity.master.kelompok.KelompokListActivity
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Activity.master.barang.BarangListActivity
import com.ahlikasir.aplikasi.kasironline.Activity.master.pelanggan.PelangganListActivity
import com.ahlikasir.aplikasi.kasironline.Activity.master.satuan.SatuanListActivity
import com.ahlikasir.aplikasi.kasironline.Activity.master.toko.TokoActivity
import kotlinx.android.synthetic.main.activity_master.*

class MasterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master)

        this.title = "Master Menu"

        cardViewToko.setOnClickListener {
            val intent = Intent(this,TokoActivity::class.java)
            startActivity(intent)
            finish()
        }

        cardView10.setOnClickListener {
            val intent = Intent(this, SatuanListActivity::class.java)
            startActivity(intent)
            finish()
        }

        cardView11.setOnClickListener {
            val intent = Intent(this, KelompokListActivity::class.java)
            startActivity(intent)
            finish()
        }

        cardView12.setOnClickListener {
            val intent = Intent(this,BarangListActivity::class.java)
            startActivity(intent)
            finish()
        }

        cardView13.setOnClickListener {
            val intent = Intent(this,PelangganListActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}