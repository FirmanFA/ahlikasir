package com.ahlikasir.aplikasi.kasironline.Activity.master.pelanggan

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.model.pelanggan.Pelanggan
import kotlinx.android.synthetic.main.activity_pelanggan_insert.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class PelangganInsertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelanggan_insert)

        val type = intent.getStringExtra("type")
        this.title = "$type Pelanggan"

        etNamaPelanggan.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val limiter = findViewById<TextView>(R.id.maxNamaPelanggan)
                limiter.text = "${etNamaPelanggan.length()}/50"
                if(etNamaPelanggan.length() == 50 || etNamaPelanggan.length() == 0){
                    limiter.setTextColor(Color.RED)
                }else{
                    limiter.setTextColor(Color.BLACK)
                }
            }
        })

        etAlamatPelanggan.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val limiter = findViewById<TextView>(R.id.maxAlamat)
                limiter.text = "${etAlamatPelanggan.length()}/70"
                if(etAlamatPelanggan.length() == 70 || etAlamatPelanggan.length() == 0){
                    limiter.setTextColor(Color.RED)
                }else{
                    limiter.setTextColor(Color.BLACK)
                }
            }
        })

        showText()

    }

    fun showText(){
        val nama = findViewById<EditText>(R.id.etNamaPelanggan)
        val alamat = findViewById<EditText>(R.id.etAlamatPelanggan)
        val telp = findViewById<EditText>(R.id.etTelpPelanggan)
        val id = intent.getStringExtra("tag")
        val type = intent.getStringExtra("type")
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        if(type.equals("Update")){
            request.getPelangganById(encodeEmail,id,token).enqueue(object: Callback<Pelanggan> {
                override fun onResponse(call: Call<Pelanggan>?, response: Response<Pelanggan>?) {
                    nama.setText(response!!.body().pelanggan)
                    alamat.setText(response.body().alamat)
                    telp.setText(response.body().notelp)
                }

                override fun onFailure(call: Call<Pelanggan>?, t: Throwable?) {
                    val alert = AlertDialog.Builder(this@PelangganInsertActivity)
                    alert.setTitle("ERROR")
                    alert.setMessage("terjadi error, coba lagi")
                    alert.setPositiveButton("RETRY",{dialog, which ->
                        showText()
                        dialog.dismiss()
                    })
                    alert.show()
                }
            })
        }
    }

    fun insertPelanggan(v: View){
        val email = Function().email(this)
        val isiPelanggan = Pelanggan(etNamaPelanggan.text.toString(),etAlamatPelanggan.text.toString(),etTelpPelanggan.text.toString(),email)
        val type = intent.getStringExtra("type")
        val request = Function().builder()
        val token = Function().token(this)
        val id = intent.getStringExtra("tag")

        if(etNamaPelanggan.text.toString() == "" || etAlamatPelanggan.text.toString() == "" || etTelpPelanggan.text.toString() == ""){
            Toast.makeText(this@PelangganInsertActivity, "ada data yang kosong", Toast.LENGTH_LONG).show()
        }else{
            if (type == "Insert") {
                request.insertPelanggan(isiPelanggan, token).enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        val alert = AlertDialog.Builder(this@PelangganInsertActivity)
                        alert.setTitle("ERROR")
                        alert.setMessage("terjadi error, coba lagi")
                        alert.setPositiveButton("RETRY",{dialog, which ->
                            insertPelanggan(v)
                            dialog.dismiss()
                        })
                        alert.show()
                    }

                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                        if (response!!.isSuccessful) {
                            Toast.makeText(this@PelangganInsertActivity, response.body().string(), Toast.LENGTH_LONG).show()
                            val intent = Intent(this@PelangganInsertActivity, PelangganListActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                })
            } else {
                request.updatePelanggan(id,isiPelanggan,token).enqueue(object: Callback<Pelanggan> {
                    override fun onFailure(call: Call<Pelanggan>?, t: Throwable?) {
                        val alert = AlertDialog.Builder(this@PelangganInsertActivity)
                        alert.setTitle("ERROR")
                        alert.setMessage("terjadi error, coba lagi")
                        alert.setPositiveButton("RETRY",{dialog, which ->
                            insertPelanggan(v)
                            dialog.dismiss()
                        })
                        alert.show()
                    }
                    override fun onResponse(call: Call<Pelanggan>?, response: Response<Pelanggan>?) {
                        if(response!!.isSuccessful){
                            if(response.body().status == "true"){
                                val intent = Intent(this@PelangganInsertActivity,PelangganListActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(this@PelangganInsertActivity,"Data masih digunakan ditempat lain",Toast.LENGTH_LONG).show()
                            }

                        }else{
                            Toast.makeText(this@PelangganInsertActivity,"Gagal Update",Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
        }


    }

    override fun onBackPressed() {
        val intent = Intent(this,PelangganListActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}
