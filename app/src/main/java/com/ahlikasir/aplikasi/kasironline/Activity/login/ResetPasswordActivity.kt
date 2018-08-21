package com.ahlikasir.aplikasi.kasironline.Activity.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.model.login.Login
import kotlinx.android.synthetic.main.activity_reset_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        hideProgress()
        resetClick()
    }

    fun reset(){
        val request = Function().builder()
        val email = etCekEmail.text.toString()
        val password = etPasswordReset.text.toString()
        val konfirmPass = etKonfirmasiPassword.text.toString()

        if(email == "" || password == "" || konfirmPass == "" || !email.contains("@")){
            Function().toast("Masukan Data Yang Benar",this)
        }else{
            if(password == konfirmPass){
                showProgress()
                val reset = Login(email,password)
                request.resetPassword(reset).enqueue(object: Callback<Login>{
                    override fun onResponse(call: Call<Login>?, response: Response<Login>?) {
                        hideProgress()
                        if (response!!.body().status == "true"){
                            val alert = AlertDialog.Builder(this@ResetPasswordActivity)
                            alert.setTitle("KONFIRMASI")
                            alert.setMessage("Email konfirmasi telah dikirimkan, harap periksa email untuk proses lebih lanjut")
                            alert.setPositiveButton("OK",{dialog, which ->
                                val intent = Intent(Intent.ACTION_MAIN)
                                try{
                                    intent.addCategory(Intent.CATEGORY_APP_EMAIL)
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
                        }else if(response.body().status == "false"){
                            Toast.makeText(this@ResetPasswordActivity,"Email Belum Terdaftar",Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(this@ResetPasswordActivity,"Something went wrong",Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Login>?, t: Throwable?) {
                        hideProgress()
                        val alert = AlertDialog.Builder(this@ResetPasswordActivity)
                        alert.setTitle("ERROR")
                        alert.setMessage("terjadi error, coba lagi")
                        alert.setPositiveButton("RETRY",{dialog, which ->
                            reset()
                            dialog.dismiss()
                        })
                        alert.show()
                    }
                })
            }else{
                Toast.makeText(this@ResetPasswordActivity,"password tidak cocok",Toast.LENGTH_LONG).show()
            }
        }


    }

    fun resetClick(){
        resetBtn.setOnClickListener {
            reset()
        }
    }

    fun showProgress(){
        progressBarReset.visibility = View.VISIBLE
        resetBtn.visibility = View.INVISIBLE
    }

    fun hideProgress(){
        progressBarReset.visibility = View.GONE
        resetBtn.visibility = View.VISIBLE
    }
}
