package com.ahlikasir.aplikasi.kasironline.Activity.master.satuan

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.model.satuan.Satuan
import kotlinx.android.synthetic.main.activity_sautuan_insert.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class SautuanInsertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sautuan_insert)

        val type = intent.getStringExtra("type")
        this.title = "$type Satuan"

        etSatuan1.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val limiter = findViewById<TextView>(R.id.tvJumlah)
                limiter.text = "${etSatuan1.length()}/5"
                if(limiter.text  == "5/5" || limiter.text  == "0/5"){
                    limiter.setTextColor(Color.RED)
                }else{
                    limiter.setTextColor(Color.BLACK)
                }
            }

        })

        etSatuan2.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val limiter = findViewById<TextView>(R.id.maxSat2)
                limiter.text = "${etSatuan2.length()}/5"
                if(limiter.text  == "5/5" || limiter.text  == "0/5"){
                    limiter.setTextColor(Color.RED)
                }else{
                    limiter.setTextColor(Color.BLACK)
                }
            }

        })

        showText()

    }

    fun insertSatuan(v:View){
        val email = Function().email(this)
        val isiSatuan = Satuan(etSatuan1.text.toString(), etNilai1.text.toString(), etSatuan2.text.toString(), etNilai2.text.toString(), email)
        val type = intent.getStringExtra("type")
        val request = Function().builder()
        val token = Function().token(this)
        val id = intent.getStringExtra("tag")

        if (etSatuan1.text.toString() == "" || etNilai1.text.toString() == "" || etSatuan2.text.toString() == "" || etNilai2.text.toString() == ""){
            Toast.makeText(this@SautuanInsertActivity, "ada data yang kosong", Toast.LENGTH_LONG).show()
        }else{
            if (type == "Insert") {
                request.insertSatuan(isiSatuan, token).enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        val alert = AlertDialog.Builder(this@SautuanInsertActivity)
                        alert.setTitle("ERROR")
                        alert.setMessage("terjadi error, coba lagi")
                        alert.setPositiveButton("RETRY",{dialog, which ->
                            insertSatuan(v)
                            dialog.dismiss()
                        })
                        alert.show()
                    }

                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                        if (response!!.isSuccessful) {
                            Toast.makeText(this@SautuanInsertActivity, response.body().string(), Toast.LENGTH_LONG).show()
                            val intent = Intent(this@SautuanInsertActivity, SatuanListActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                })
            } else {
                request.updateSatuan(id,isiSatuan,token).enqueue(object: Callback<Satuan> {
                    override fun onFailure(call: Call<Satuan>?, t: Throwable?) {
                        val alert = AlertDialog.Builder(this@SautuanInsertActivity)
                        alert.setTitle("ERROR")
                        alert.setMessage("terjadi error, coba lagi")
                        alert.setPositiveButton("RETRY",{dialog, which ->
                            insertSatuan(v)
                            dialog.dismiss()
                        })
                        alert.show()
                    }
                    override fun onResponse(call: Call<Satuan>?, response: Response<Satuan>?) {
                        if(response!!.isSuccessful){
                            if(response.body().status == "true"){
                                val intent = Intent(this@SautuanInsertActivity,SatuanListActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(this@SautuanInsertActivity,"Data masih digunakan ditempat lain",Toast.LENGTH_LONG).show()
                            }

                        }else{
                            Toast.makeText(this@SautuanInsertActivity,"Gagal Update",Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
        }


    }




    fun showText(){
        val satuan1 = findViewById<EditText>(R.id.etSatuan1)
        val nilai1 = findViewById<EditText>(R.id.etNilai1)
        val satuan2 = findViewById<EditText>(R.id.etSatuan2)
        val nilai2 = findViewById<EditText>(R.id.etNilai2)
        val id = intent.getStringExtra("tag")
        val type = intent.getStringExtra("type")
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        if(type.equals("Update")){
            request.getSatuanById(encodeEmail,id,token).enqueue(object: Callback<Satuan>{
                override fun onResponse(call: Call<Satuan>?, response: Response<Satuan>?) {
                    satuan1.setText(response!!.body().satuan1)
                    nilai1.setText(response.body().nilai1)
                    satuan2.setText(response.body().satuan2)
                    nilai2.setText(response.body().nilai2)
                }

                override fun onFailure(call: Call<Satuan>?, t: Throwable?) {
                    val alert = AlertDialog.Builder(this@SautuanInsertActivity)
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

    override fun onBackPressed() {
        val intent = Intent(this,SatuanListActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}
