package com.ahlikasir.aplikasi.kasironline.Activity.transaksi.user

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
import com.ahlikasir.aplikasi.kasironline.Activity.transaksi.TransaksiActivity
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.adapter.transaksi.UserAdapter
import com.ahlikasir.aplikasi.kasironline.model.transaksi.User
import kotlinx.android.synthetic.main.activity_user_list.*
import retrofit2.Call
import retrofit2.Response
import java.net.URLEncoder

class UserListActivity : AppCompatActivity() {

    lateinit var adapter:UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        hideProgressBar()

        val jumlahData = findViewById<TextView>(R.id.tJumlahUser)

        loadData()

        insertUserBtn.setOnClickListener {
            val intent = Intent(this,UserInsertActivity::class.java)
            intent.putExtra("type","Insert")
            startActivity(intent)
            finish()
        }

        eCariUser.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(eCariUser.text)
                jumlahData.text = "Jumlah Data : ${adapter.itemCount}"
            }
        })
    }

    private fun loadData() {
        showProgressBar()
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        request.getUser(encodeEmail,token).enqueue(object: retrofit2.Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
                hideProgressBar()
                if (response!!.isSuccessful){
                    adapter = UserAdapter(this@UserListActivity,response.body(),{User ->

                        deleteUser(User)
                        adapter.notifyDataSetChanged()

                    })
                    recListUser.adapter = adapter
                    val layoutmanager = LinearLayoutManager(this@UserListActivity)
                    recListUser.layoutManager = layoutmanager
                    recListUser.setHasFixedSize(true)
                    val jumlahData = findViewById<TextView>(R.id.tJumlahUser)
                    jumlahData.text = "Jumlah Data : ${response.body().size}"
                }else{
                    Toast.makeText(this@UserListActivity,"Tidak Ada Data", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                hideProgressBar()
                val alert = AlertDialog.Builder(this@UserListActivity)
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

    private fun deleteUser(User: User) {
        val request = Function().builder()
        val token = Function().token(this)
        val alert = AlertDialog.Builder(this@UserListActivity)
        alert.setTitle("DELETE")
        alert.setMessage("Are You Sure?")
        alert.setPositiveButton("YES",{dialog, which ->
            showProgressBar()
            request.deleteUser(User.iduser,token).enqueue(object : retrofit2.Callback<User> {
                override fun onResponse(call: Call<User>?, response: Response<User>?) {
                    hideProgressBar()
                    if(response!!.isSuccessful){
                        if(response.body().status == "true"){
                            Toast.makeText(this@UserListActivity,"Berhasil Delete", Toast.LENGTH_LONG).show()
                            loadData()
                        }else{
                            Toast.makeText(this@UserListActivity,"Data masih digunakan ditempat lain", Toast.LENGTH_LONG).show()
                        }

                    }else{
                        Toast.makeText(this@UserListActivity,"Gagal Delete", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<User>?, t: Throwable?) {
                    hideProgressBar()
                    val alert = AlertDialog.Builder(this@UserListActivity)
                    alert.setTitle("ERROR")
                    alert.setMessage("terjadi error, coba lagi")
                    alert.setPositiveButton("RETRY",{dialog, which ->
                        deleteUser(User)
                        dialog.dismiss()
                    })
                    alert.show()
                }

            })
            dialog.dismiss()
        })
        alert.setNegativeButton("NO",{dialog, which ->
            dialog.dismiss()
        })
        alert.show()
    }

    fun showProgressBar(){
        progressUser.visibility =  View.VISIBLE
        recListUser.visibility = View.INVISIBLE
    }

    fun hideProgressBar(){
        progressUser.visibility = View.INVISIBLE
        recListUser.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        val intent = Intent(this,TransaksiActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}
