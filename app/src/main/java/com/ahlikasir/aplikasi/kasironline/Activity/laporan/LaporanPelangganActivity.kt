package com.ahlikasir.aplikasi.kasironline.Activity.laporan

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.adapter.laporan.LaporanPelangganAdapter
import com.ahlikasir.aplikasi.kasironline.model.pelanggan.Pelanggan
import kotlinx.android.synthetic.main.activity_pelanggan_list.*
import retrofit2.Call
import retrofit2.Response
import java.net.URLEncoder

class LaporanPelangganActivity : AppCompatActivity() {

    lateinit var adapter:LaporanPelangganAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan_pelanggan)

        hideProgressBar()

        val jumlahData = findViewById<TextView>(R.id.tJumlahPelanggan)

        loadData()

        insertPelangganBtn.setOnClickListener {
            val intent = Intent(this, ExportExcelActivity::class.java)
            intent.putExtra("exportType","pelanggan")
            startActivity(intent)
            finish()
        }

        eCariPelanggan.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(eCariPelanggan.text)
                adapter.notifyDataSetChanged()
                jumlahData.text = "Jumlah Data : ${adapter.itemCount}"
            }
        })

    }

    private fun loadData() {
        showProgressBar()
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        request.getPelanggan(encodeEmail,token).enqueue(object: retrofit2.Callback<List<Pelanggan>> {
            override fun onResponse(call: Call<List<Pelanggan>>?, response: Response<List<Pelanggan>>?) {
                hideProgressBar()
                if (response!!.isSuccessful){
                    adapter = LaporanPelangganAdapter(this@LaporanPelangganActivity,response.body())
                    recListPelanggan.adapter = adapter
                    val layoutmanager = LinearLayoutManager(this@LaporanPelangganActivity)
                    recListPelanggan.layoutManager = layoutmanager
                    recListPelanggan.setHasFixedSize(true)
                    val jumlahData = findViewById<TextView>(R.id.tJumlahPelanggan)
                    adapter.notifyDataSetChanged()
                    jumlahData.text = "Jumlah Data : ${adapter.itemCount}"
                }else{
                    Toast.makeText(this@LaporanPelangganActivity,"Tidak Ada Data", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Pelanggan>>?, t: Throwable?) {
                hideProgressBar()
                val alert = AlertDialog.Builder(this@LaporanPelangganActivity)
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

    fun showProgressBar(){
        progressPelanggan.visibility =  View.VISIBLE
        recListPelanggan.visibility = View.INVISIBLE
    }

    fun hideProgressBar(){
        progressPelanggan.visibility = View.INVISIBLE
        recListPelanggan.visibility = View.VISIBLE
    }

}
