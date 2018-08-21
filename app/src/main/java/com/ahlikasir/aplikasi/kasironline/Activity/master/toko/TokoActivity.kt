package com.ahlikasir.aplikasi.kasironline.Activity.master.toko

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.Activity.main.MainActivity
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.Activity.master.MasterActivity
import com.ahlikasir.aplikasi.kasironline.model.toko.Toko
import com.ahlikasir.aplikasi.kasironline.model.toko.TokoType
import com.ahlikasir.aplikasi.kasironline.model.toko.TokoUpIn
import kotlinx.android.synthetic.main.activity_toko.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.net.URLEncoder

class TokoActivity : AppCompatActivity() {

    lateinit var sharedPrefrences:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toko)

        initShared()
        val type = intent.getStringExtra("type")
        if (type == "insert"){
            this.title = "Insert Identitas"
        }else{
            this.title = "Update Identitas"
        }


        limit()

        loadData()

    }

    private fun loadData() {
        val request = Function().builder()
        val token = getSharedPrefrences("token","")
        val email = Function().email(this)
        val isiToko = Toko(email)
        val encodeEmail = URLEncoder.encode("oon@gmail.com","UTF-8")
        request.cekEmail(isiToko,token).enqueue(object : retrofit2.Callback<TokoType>{
            override fun onFailure(call: Call<TokoType>?, t: Throwable?) {
                val alert = AlertDialog.Builder(this@TokoActivity)
                alert.setTitle("ERROR")
                alert.setMessage("terjadi error, coba lagi")
                alert.setPositiveButton("RETRY",{dialog, which ->
                    loadData()
                    dialog.dismiss()
                })
                alert.show()
            }

            override fun onResponse(call: Call<TokoType>?, response: Response<TokoType>?) {
                if(response!!.isSuccessful){
                    val type = response.body().status
                    if(type == "Insert"){
                        tokoUpIn.setOnClickListener{insertToko(isiToko.emailtoko)}
                    }else if(type == "Update"){
                        val encodeEmail = URLEncoder.encode(email,"UTF-8")
                        request.getTokoByEmail(encodeEmail,token).enqueue(object: retrofit2.Callback<TokoUpIn>{
                            override fun onResponse(call: Call<TokoUpIn>?, response: Response<TokoUpIn>?) {
                                eNama.setText(response!!.body().toko)
                                eAlamat.setText(response.body().alamat)
                                eTelp.setText(response.body().telp)
                                cap1.setText(response.body().promo1)
                                cap2.setText(response.body().promo2)
                                tokoUpIn.setOnClickListener{updateToko(isiToko.emailtoko)}
                            }

                            override fun onFailure(call: Call<TokoUpIn>?, t: Throwable?) {

                            }
                        })
                    }else{
                        Toast.makeText(this@TokoActivity,"something went wrong",Toast.LENGTH_LONG).show()
                        tokoUpIn.isEnabled = false
                    }
                }else{
                    Toast.makeText(this@TokoActivity,"something went wrong",Toast.LENGTH_LONG).show()
                    tokoUpIn.isEnabled = false
                }
            }
        })
    }

    fun updateToko(email:String){
        val request = Function().builder()
        val token = getSharedPrefrences("token","")
        val isiToko = TokoUpIn(eNama.text.toString(),eAlamat.text.toString(),eTelp.text.toString(),cap1.text.toString(),cap2.text.toString(),email)

        if(eNama.text.toString() == ""||eAlamat.text.toString() == ""||eTelp.text.toString()== "" ||cap1.text.toString() == "" || cap2.text.toString() == ""){
            Function().toast("Ada Data Yang kosong",this)
        }else{
            request.updateToko(isiToko,token).enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    Toast.makeText(this@TokoActivity,"Berhasil Update",Toast.LENGTH_LONG).show()
                    Function().setSharedPrefrences("tokoHeader",eNama.text.toString(),this@TokoActivity)
                    Function().setSharedPrefrences("telpHeader",eTelp.text.toString(),this@TokoActivity)
                }

                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    val alert = AlertDialog.Builder(this@TokoActivity)
                    alert.setTitle("ERROR")
                    alert.setMessage("terjadi error, coba lagi")
                    alert.setPositiveButton("RETRY",{dialog, which ->
                        updateToko(email)
                        dialog.dismiss()
                    })
                    alert.show()
                }
            })
        }


    }

    fun insertToko(email: String){
        val request = Function().builder()
        val token = getSharedPrefrences("token","")
        val isiToko = TokoUpIn(eNama.text.toString(),eAlamat.text.toString(),eTelp.text.toString(),cap1.text.toString(),cap2.text.toString(),email)

        if(eNama.text.toString() == ""||eAlamat.text.toString() == ""||eTelp.text.toString()== "" ||cap1.text.toString() == "" || cap2.text.toString() == ""){
            Function().toast("Ada Data Yang kosong",this)
        }else{
            request.insertToko(isiToko,token).enqueue(object: retrofit2.Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    val alert = AlertDialog.Builder(this@TokoActivity)
                    alert.setTitle("ERROR")
                    alert.setMessage("terjadi error, coba lagi")
                    alert.setPositiveButton("RETRY",{dialog, which ->
                        insertToko(email)
                        dialog.dismiss()
                    })
                    alert.show()
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response!!.isSuccessful){
                        setSharedPrefrences("tokoHeader",eNama.text.toString())
                        setSharedPrefrences("telpHeader",eTelp.text.toString())
                        val intent = Intent(this@TokoActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            })
        }


    }

    fun limit(){
        eNama.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val limiter = findViewById<TextView>(R.id.maxToko)
                limiter.text = "${eNama.length()}/30"
                if(eNama.length() == 30 || limiter.length() == 0){
                    limiter.setTextColor(Color.RED)
                }else{
                    limiter.setTextColor(Color.BLACK)
                }
            }

        })

        eAlamat.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val limiter = findViewById<TextView>(R.id.maxTokoAlamat)
                val limited = findViewById<EditText>(R.id.eAlamat)
                limiter.text = "${limited.length()}/30"
                if(limited.length() == 30 || limiter.length() == 0){
                    limiter.setTextColor(Color.RED)
                }else{
                    limiter.setTextColor(Color.BLACK)
                }
            }

        })

        cap1.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val limiter = findViewById<TextView>(R.id.maxCap1)
                val limited = findViewById<EditText>(R.id.cap1)
                limiter.text = "${limited.length()}/30"
                if(limited.length() == 30 || limiter.length() == 0){
                    limiter.setTextColor(Color.RED)
                }else{
                    limiter.setTextColor(Color.BLACK)
                }
            }

        })

        cap2.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val limiter = findViewById<TextView>(R.id.maxCap2)
                val limited = findViewById<EditText>(R.id.cap2)
                limiter.text = "${limited.length()}/30"
                if(limited.length() == 30 || limiter.length() == 0){
                    limiter.setTextColor(Color.RED)
                }else{
                    limiter.setTextColor(Color.BLACK)
                }
            }

        })


    }

    override fun onBackPressed() {

        val intent = Intent(this@TokoActivity, MasterActivity::class.java)
        startActivity(intent)
        finish()

        super.onBackPressed()
    }

    fun initShared(){
        sharedPrefrences = getSharedPreferences("coba", Context.MODE_PRIVATE)
    }

    fun setSharedPrefrences(name:String,value:String){
        val editor = sharedPrefrences.edit()
        editor.putString(name,value)
        editor.apply()
    }

    fun getSharedPrefrences(name:String,default:String):String{
        return sharedPrefrences.getString(name,default)
    }
}
