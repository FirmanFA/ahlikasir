package com.ahlikasir.aplikasi.kasironline.Activity.transaksi.user

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.model.transaksi.User
import kotlinx.android.synthetic.main.activity_user_insert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInsertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_insert)

        val type = intent.getStringExtra("type")
        this.title = "$type User"

        etPassword.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val limiter = findViewById<TextView>(R.id.maxPassword)
                limiter.text = "${etPassword.length()}/30"
                if(etPassword.length() == 30 || limiter.length() == 0){
                    limiter.setTextColor(Color.RED)
                }else{
                    limiter.setTextColor(Color.BLACK)
                }
            }

        })

        etKonfirmasi.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val limiter = findViewById<TextView>(R.id.maxKonPassword)
                limiter.text = "${etKonfirmasi.length()}/30"
                if(etKonfirmasi.length() == 30 || limiter.length() == 0){
                    limiter.setTextColor(Color.RED)
                }else{
                    limiter.setTextColor(Color.BLACK)
                }
            }

        })
    }

    fun insertUser(v: View){
        val email = Function().email(this)
        val isiUser = User(etEmail.text.toString(),etNotelpUser.text.toString(),email,etPassword.text.toString())
        val type = intent.getStringExtra("type")
        val request = Function().builder()
        val token = Function().token(this)

        if(etEmail.text.toString() == "" || etPassword.text.toString() == "" || etKonfirmasi.text.toString() == ""){
            Toast.makeText(this@UserInsertActivity, "ada data yang kosong", Toast.LENGTH_LONG).show()
        }else if(etPassword.text.toString() != etKonfirmasi.text.toString()){
            Toast.makeText(this@UserInsertActivity, "Password Tidak Cocok", Toast.LENGTH_LONG).show()
        }else{
            if (type == "Insert") {
                request.insertUser(isiUser, token).enqueue(object : Callback<User> {
                    override fun onFailure(call: Call<User>?, t: Throwable?) {
                        val alert = AlertDialog.Builder(this@UserInsertActivity)
                        alert.setTitle("ERROR")
                        alert.setMessage("terjadi error, coba lagi")
                        alert.setPositiveButton("RETRY",{dialog, _ ->
                            insertUser(v)
                            dialog.dismiss()
                        })
                        alert.show()
                    }

                    override fun onResponse(call: Call<User>?, response: Response<User>?) {
                        if (response!!.isSuccessful) {

                            if(response.body().status == "used"){
                                Toast.makeText(this@UserInsertActivity, "Email Sudah Ada", Toast.LENGTH_LONG).show()
                            }else if(response.body().status == "true"){
                                Toast.makeText(this@UserInsertActivity, "Berhasil Insert", Toast.LENGTH_LONG).show()
                                val intent = Intent(this@UserInsertActivity, UserListActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(this@UserInsertActivity, "Something Went Wrong", Toast.LENGTH_LONG).show()
                            }


                        }
                    }
                })
            }
        }


    }

    override fun onBackPressed() {
        val intent = Intent(this,UserListActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}
