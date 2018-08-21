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
import com.ahlikasir.aplikasi.kasironline.adapter.laporan.LaporanBarangAdapter
import com.ahlikasir.aplikasi.kasironline.model.barang.Barang
import kotlinx.android.synthetic.main.activity_laporan_barang.*
import retrofit2.Call
import retrofit2.Response
import java.net.URLEncoder

class LaporanBarangActivity : AppCompatActivity() {

    lateinit var adapter: LaporanBarangAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan_barang)

        hideProgressBar()

        val jumlahdata = findViewById<TextView>(R.id.tJumlahbarang)

        loadData()




        tambahBarang.setOnClickListener {
            val intent = Intent(this,ExportExcelActivity::class.java)
            intent.putExtra("exportType","barang")
            startActivity(intent)
        }

        eCari.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(eCari.text.toString())
                adapter.notifyDataSetChanged()
                jumlahdata.text = "Jumlah Data : ${adapter.itemCount}"
            }
        })
    }

    private fun loadData() {
        showProgressBar()
        val jumlahdata = findViewById<TextView>(R.id.tJumlahbarang)
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        request.getBarang(encodeEmail,token).enqueue(object : retrofit2.Callback<List<Barang>>{
            override fun onResponse(call: Call<List<Barang>>?, response: Response<List<Barang>>?) {
                hideProgressBar()
                if (response!!.isSuccessful){
                    adapter = LaporanBarangAdapter(this@LaporanBarangActivity,response.body())
                    recListbarang.adapter = adapter
                    val layoutmanager = LinearLayoutManager(this@LaporanBarangActivity)
                    recListbarang.layoutManager = layoutmanager
                    recListbarang.setHasFixedSize(true)
                    adapter.notifyDataSetChanged()
                    jumlahdata.text = "Jumlah Data : ${adapter.itemCount}"
                }else{
                    Toast.makeText(this@LaporanBarangActivity,"Tidak Ada data", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Barang>>?, t: Throwable?) {
                hideProgressBar()
                val alert = AlertDialog.Builder(this@LaporanBarangActivity)
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
        progressBarang.visibility =  View.VISIBLE
        recListbarang.visibility = View.INVISIBLE
    }

    fun hideProgressBar(){
        progressBarang.visibility = View.INVISIBLE
        recListbarang.visibility = View.VISIBLE
    }
}
