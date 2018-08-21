package com.ahlikasir.aplikasi.kasironline.Activity.laporan

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ahlikasir.aplikasi.kasironline.R
import kotlinx.android.synthetic.main.activity_laporan.*

class LaporanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

        eventHandler()

    }

    fun eventHandler(){
        cardViewLapBarang.setOnClickListener {
            val intent = Intent(this,LaporanBarangActivity::class.java)
            startActivity(intent)
        }

        cardViewLapPelanggan.setOnClickListener {
            val intent = Intent(this,LaporanPelangganActivity::class.java)
            startActivity(intent)
        }

        cardViewLapPenjualan.setOnClickListener {
            val intent = Intent(this,LaporanPenjualanActivity::class.java)
            startActivity(intent)
        }

        cardViewLapPendapatan.setOnClickListener {
            val intent = Intent(this,LaporanPendapatanActivity::class.java)
            startActivity(intent)
        }

        cardViewLapPerbarang.setOnClickListener {
            val intent = Intent(this,LaporanPerbarangActivity::class.java)
            startActivity(intent)
        }

        cardViewFaktur.setOnClickListener {
            val intent = Intent(this,LaporanPerfakturActivity::class.java)
            startActivity(intent)
        }
    }
}
