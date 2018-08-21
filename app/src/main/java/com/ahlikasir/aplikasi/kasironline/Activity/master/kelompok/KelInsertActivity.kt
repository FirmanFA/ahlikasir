package com.ahlikasir.aplikasi.kasironline.Activity.master.kelompok

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.model.kelompok.Kelompok
import kotlinx.android.synthetic.main.activity_kel_insert.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class KelInsertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kel_insert)

        val type = intent.getStringExtra("type")
        val id = intent.getStringExtra("tag")
        this.title = "$type Kelompok"

        showText()
        simpanClick()

        etKelompok.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val limiter = findViewById<TextView>(R.id.maxNamaKelompok)
                limiter.text = "${etKelompok.length()}/30"
                if(etKelompok.length() == 30 || limiter.length() == 0){
                    limiter.setTextColor(Color.RED)
                }else{
                    limiter.setTextColor(Color.BLACK)
                }
            }

        })
    }

    fun simpanClick(){
        simpanKel.setOnClickListener {
            val type = intent.getStringExtra("type")
            val id = intent.getStringExtra("tag")
            val email = Function().email(this)
            val isiKelompok = Kelompok("${etKelompok.text}",email)
            val request = Function().builder()
            val token = Function().token(this)

            if(etKelompok.text.toString() == ""){
                Toast.makeText(this@KelInsertActivity,"ada data yang kosong",Toast.LENGTH_LONG).show()
            }else{
                if(type == "Insert"){
                    request.insertKelompok(isiKelompok,token).enqueue(object: Callback<ResponseBody>{
                        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                            val alert = AlertDialog.Builder(this@KelInsertActivity)
                            alert.setTitle("ERROR")
                            alert.setMessage("terjadi error, coba lagi")
                            alert.setPositiveButton("RETRY",{dialog, which ->
                                simpanClick()
                                dialog.dismiss()
                            })
                            alert.show()
                        }

                        override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                            if (response!!.isSuccessful) {
                                Toast.makeText(this@KelInsertActivity,response.body().string(),Toast.LENGTH_LONG).show()
                                val intent = Intent(this@KelInsertActivity,KelompokListActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    })
                }else{
                    request.updateKelompok(id,isiKelompok,token).enqueue(object: Callback<Kelompok> {
                        override fun onFailure(call: Call<Kelompok>?, t: Throwable?) {
                            val alert = AlertDialog.Builder(this@KelInsertActivity)
                            alert.setTitle("ERROR")
                            alert.setMessage("terjadi error, coba lagi")
                            alert.setPositiveButton("RETRY",{dialog, which ->
                                simpanClick()
                                dialog.dismiss()
                            })
                            alert.show()
                        }

                        override fun onResponse(call: Call<Kelompok>?, response: Response<Kelompok>?) {
                            if(response!!.isSuccessful){
                                if(response.body().status == "true"){
                                    val intent = Intent(this@KelInsertActivity,KelompokListActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }else{
                                    Toast.makeText(this@KelInsertActivity,"Data masih digunakan ditempat lain",Toast.LENGTH_LONG).show()
                                }

                            }else{
                                Toast.makeText(this@KelInsertActivity,"Gagal Update",Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
            }
        }
    }

    fun showText(){
        val id = intent.getStringExtra("tag")
        val type = intent.getStringExtra("type")
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        if(type.equals("Update")){
            request.getKelompokById(encodeEmail,id,token).enqueue(object: Callback<Kelompok> {
                override fun onFailure(call: Call<Kelompok>?, t: Throwable?) {
                    val alert = AlertDialog.Builder(this@KelInsertActivity)
                    alert.setTitle("ERROR")
                    alert.setMessage("terjadi error, coba lagi")
                    alert.setPositiveButton("RETRY",{dialog, which ->
                        showText()
                        dialog.dismiss()
                    })
                    alert.show()
                }

                override fun onResponse(call: Call<Kelompok>?, response: Response<Kelompok>?) {
                    val kel = findViewById<EditText>(R.id.etKelompok)
                    kel.setText(response!!.body().kelompok)
                }
            })
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this,KelompokListActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}
