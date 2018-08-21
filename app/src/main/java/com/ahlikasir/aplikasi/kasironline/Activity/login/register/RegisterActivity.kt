package com.ahlikasir.aplikasi.kasironline.Activity.login.register

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.model.login.Login
import com.ahlikasir.aplikasi.kasironline.model.login.Register
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        hideProgressBar()
        daftarClick()
    }

    fun daftarClick(){

        daftarBtn.setOnClickListener {
            showProgressBar()
            val email = findViewById<EditText>(R.id.emailRegis)
            val notelp = findViewById<EditText>(R.id.telpRegis)
            val password = findViewById<EditText>(R.id.passRegis)
            val confirmpass = findViewById<EditText>(R.id.confPassRegis)
            if(email.text.toString() == "" || notelp.text.toString() == "" || password.text.toString() == "" || confirmpass.text.toString() == "" || !email.text.toString().contains("@")){
                hideProgressBar()
                Toast.makeText(this@RegisterActivity,"Masukan Data Dengan Benar", Toast.LENGTH_SHORT).show()
            }else if(password.text.toString() != confirmpass.text.toString()){
                hideProgressBar()
                Toast.makeText(this@RegisterActivity,"password tidak cocok", Toast.LENGTH_SHORT).show()
            }else{
                val request = Function().builder()
                val daftar = Register(email.text.toString(),notelp.text.toString(),password.text.toString())
                request.register(daftar).enqueue(object: Callback<Login> {
                    override fun onResponse(call: Call<Login>?, response: Response<Login>?) {
                        if(response!!.body().status == "used"){
                            hideProgressBar()
                            Toast.makeText(this@RegisterActivity,"email sudah dipakai", Toast.LENGTH_SHORT).show()
                        }else{
                            hideProgressBar()
                            val alert = AlertDialog.Builder(this@RegisterActivity)
                            alert.setTitle("KONFIRMASI")
                            alert.setMessage("Email konfirmasi telah dikirimkan, harap periksa email untuk regristasi lebih lanjut" + "\n"+
                                                "NB : Jika Saat Membuka page aktivasi di browser ada peringatan, tekan lanjutkan / proceed." +
                                                " baca email untuk keterangan lebih lanjut")
                            alert.setPositiveButton("OK",{dialog, which ->
                                val intent = Intent(Intent.ACTION_MAIN)
                                intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                                try{
                                    startActivity(intent)
                                    dialog.dismiss()
                                    finish()
                                }catch (e:Exception){
                                    intent.addCategory(Intent.CATEGORY_APP_BROWSER)
                                    startActivity(intent)
                                    dialog.dismiss()
                                    finish()
                                }
                            })
                            alert.show()
                        }
                    }

                    override fun onFailure(call: Call<Login>?, t: Throwable?) {
                        hideProgressBar()
                        Toast.makeText(this@RegisterActivity,"periksa koneksi anda", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    fun showProgressBar(){
        daftarBtn.visibility = View.GONE
        progressBar2.visibility = View.VISIBLE
    }

    fun hideProgressBar(){
        daftarBtn.visibility = View.VISIBLE
        progressBar2.visibility = View.GONE
    }

}
