package com.ahlikasir.aplikasi.kasironline.Activity.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.Activity.main.MainActivity
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.Activity.login.register.RegisterActivity
import com.ahlikasir.aplikasi.kasironline.Activity.master.toko.TokoActivity
import com.ahlikasir.aplikasi.kasironline.model.login.Login
import com.ahlikasir.aplikasi.kasironline.model.toko.TokoUpIn
import com.ahlikasir.aplikasi.kasironline.model.transaksi.User
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPrefrences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        val email = findViewById<EditText>(R.id.etLoginEmail)
        val etPassword = findViewById<EditText>(R.id.etLoginPassword)

        showButton()

        init()

        loginBtn.setOnClickListener{
            showProgressBar()
            if(email.text.toString() == "" || etPassword.text.toString() == "" || !email.text.toString().contains("@")){
                Toast.makeText(this@LoginActivity,"Masukan Data Dengan Benar", Toast.LENGTH_SHORT).show()
                showButton()
            }else{
                val request = Function().builder()
                val login = Login(email.text.toString(), etPassword.text.toString())
                request.login(login).enqueue(object : Callback<Login>{
                    override fun onResponse(call: Call<Login>?, response: Response<Login>?) {
                        if (response!!.isSuccessful){
                            if (response.body().status == "sukses"){
                                val token = response.body().token
                                val emailEnc = URLEncoder.encode(email.text.toString(),"UTF-8")
                                val emailParent = response.body().emailparent
                                val emailParentEnc = URLEncoder.encode(emailParent,"UTF-8")
                                request.cekUser(emailEnc,token).enqueue(object: Callback<User>{

                                    override fun onFailure(call: Call<User>?, t: Throwable?) {
                                        Toast.makeText(this@LoginActivity,"something went wrong", Toast.LENGTH_SHORT).show()
                                        showButton()
                                    }

                                    override fun onResponse(call: Call<User>?, response: Response<User>?) {
                                        if(response!!.isSuccessful){
                                            if (response.body().status == "true"){
                                                setSharedPrefrences("emailCek","true")
                                            }else{
                                                setSharedPrefrences("emailCek","false")
                                            }

                                            request.getTokoByEmail(emailParentEnc,token).enqueue(object : Callback<TokoUpIn>{
                                                override fun onResponse(call: Call<TokoUpIn>?, response: Response<TokoUpIn>?) {
                                                    if(response!!.isSuccessful){

                                                        if(response.body().status == "Insert"){
                                                            val intent = Intent(this@LoginActivity,TokoActivity::class.java)
                                                            intent.putExtra("type","insert")
                                                            intent.putExtra("email",emailParent)
                                                            intent.putExtra("token",token)
                                                            setSharedPrefrences("token",token)
                                                            setSharedPrefrences("email",emailParent)
                                                            startActivity(intent)
                                                            finish()
                                                        }else{
                                                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                                            intent.putExtra("email",emailParent)
                                                            intent.putExtra("token",token)
                                                            setSharedPrefrences("token",token)
                                                            setSharedPrefrences("email",emailParent)
                                                            setSharedPrefrences("tokoHeader",response.body().toko)
                                                            setSharedPrefrences("telpHeader",response.body().telp)
                                                            startActivity(intent)
                                                            finish()
                                                        }


                                                    }
                                                }

                                                override fun onFailure(call: Call<TokoUpIn>?, t: Throwable?) {
                                                    Toast.makeText(this@LoginActivity,"something went wrong", Toast.LENGTH_SHORT).show()
                                                    showButton()
                                                }
                                            })
                                        }else{
                                            Toast.makeText(this@LoginActivity,"something went wrong", Toast.LENGTH_SHORT).show()
                                            showButton()
                                        }

                                    }
                                })

                            }else{
                                Toast.makeText(this@LoginActivity,response.body().status, Toast.LENGTH_SHORT).show()
                                showButton()
                            }
                        }else{
                            Toast.makeText(this@LoginActivity,"something went wrong", Toast.LENGTH_SHORT).show()
                            showButton()
                        }
                    }

                    override fun onFailure(call: Call<Login>?, t: Throwable?) {
                        Toast.makeText(this@LoginActivity,"periksa koneksi anda", Toast.LENGTH_SHORT).show()
                        showButton()



                    }
                })


            }
        }

        btnDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        tvReset.setOnClickListener{
            val intent = Intent(this,ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun init(){
        sharedPrefrences = getSharedPreferences("coba", Context.MODE_PRIVATE)
    }

    fun setSharedPrefrences(name:String,value:String){
        val editor = sharedPrefrences.edit()
        editor.putString(name,value)
        editor.apply()
    }

    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
        loginBtn.visibility = View.INVISIBLE
    }

    fun showButton(){
        progressBar.visibility = View.INVISIBLE
        loginBtn.visibility = View.VISIBLE
    }

}
