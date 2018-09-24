package com.ahlikasir.aplikasi.kasironline.Activity.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Activity.laporan.LaporanActivity
import com.ahlikasir.aplikasi.kasironline.Activity.login.LoginActivity
import com.ahlikasir.aplikasi.kasironline.Activity.master.MasterActivity
import com.ahlikasir.aplikasi.kasironline.Activity.transaksi.TransaksiActivity
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.model.toko.TokoUpIn
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    lateinit var sharedPrefrences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        val username = getSharedPrefrences("email","")
        if(username == "logout" || username == ""){
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        this.title = "Home"

        imageView2.setOnClickListener {v ->
            val popupmenu = PopupMenu(this,v)
            popupmenu.setOnMenuItemClickListener {item ->
                when(item.itemId){
                    R.id.logout -> {
                        logout()
                        true
                    }

                    R.id.quit -> {
                        finish()
                        true
                    }

                    else -> {
                        false
                    }
                }

            }
            popupmenu.inflate(R.menu.master_menu)
            popupmenu.show()
        }

        cardViewMaster.setOnClickListener{
            val request = Function().builder()
            val emailraw = Function().email(this)
            val token = Function().token(this)
            val email = URLEncoder.encode(emailraw,"UTF-8")

            request.cekMaxData("0",email,token).enqueue(object: Callback<TokoUpIn>{
                override fun onResponse(call: Call<TokoUpIn>?, response: Response<TokoUpIn>?) {
                    if(response!!.isSuccessful){
                        if(response.body().status == "true"){
                            val intent = Intent(this@MainActivity, MasterActivity::class.java)
                            startActivity(intent)
                        }else{
                            Function().toast("Mohon Maaf Data Anda Sudah Melebihi Kapasitas",this@MainActivity)
                        }
                    }
                }

                override fun onFailure(call: Call<TokoUpIn>?, t: Throwable?) {
                    Function().toast("Something Went Wrong, Try Again",this@MainActivity)
                }
            })


        }

        cardViewTransaksi.setOnClickListener {

            val request = Function().builder()
            val emailraw = Function().email(this)
            val token = Function().token(this)
            val email = URLEncoder.encode(emailraw,"UTF-8")

            request.cekMaxData("1",email,token).enqueue(object: Callback<TokoUpIn>{
                override fun onResponse(call: Call<TokoUpIn>?, response: Response<TokoUpIn>?) {
                    if(response!!.isSuccessful){
                        if(response.body().status == "true"){
                            val intent = Intent(this@MainActivity, TransaksiActivity::class.java)
                            startActivity(intent)
                        }else{
                            Function().toast("Mohon Maaf Data Anda Sudah Melebihi Kapasitas",this@MainActivity)
                        }
                    }
                }

                override fun onFailure(call: Call<TokoUpIn>?, t: Throwable?) {
                    Function().toast("Something Went Wrong, Try Again",this@MainActivity)
                }
            })


        }

        cardViewLaporan.setOnClickListener {
            val intent = Intent(this, LaporanActivity::class.java)
            startActivity(intent)
        }

        val namatoko = getSharedPrefrences("tokoHeader","")
        val telptoko = getSharedPrefrences("telpHeader","")
        textView111.text = namatoko
        textView26.text = telptoko



    }

    private fun logout() {
        setSharedPrefrences("email","logout")
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    //buat sharedprefrence untuk  login

    fun init(){
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
