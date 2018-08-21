package com.ahlikasir.aplikasi.kasironline.Activity.master.pelanggan

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
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.adapter.pelanggan.PelangganAdapter
import com.ahlikasir.aplikasi.kasironline.Activity.master.MasterActivity
import com.ahlikasir.aplikasi.kasironline.model.pelanggan.Pelanggan
import kotlinx.android.synthetic.main.activity_pelanggan_list.*
import retrofit2.Call
import retrofit2.Response
import java.net.URLEncoder

class PelangganListActivity : AppCompatActivity() {

    lateinit var adapter:PelangganAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelanggan_list)

        hideProgressBar()

        val jumlahData = findViewById<TextView>(R.id.tJumlahPelanggan)

        loadData()

        insertPelangganBtn.setOnClickListener {
            val intent = Intent(this,PelangganInsertActivity::class.java)
            intent.putExtra("type","Insert")
            startActivity(intent)
            finish()
        }

        eCariPelanggan.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(eCariPelanggan.text)
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
        request.getPelanggan(encodeEmail,token).enqueue(object: retrofit2.Callback<List<Pelanggan>> {
            override fun onResponse(call: Call<List<Pelanggan>>?, response: Response<List<Pelanggan>>?) {
                hideProgressBar()
                if (response!!.isSuccessful){
                    adapter = PelangganAdapter(this@PelangganListActivity,response!!.body(),{pelanggan ->

                        deletePelanggan(pelanggan)
                        adapter.notifyDataSetChanged()

                    },{pelanggan ->
                        val intent = Intent(this@PelangganListActivity, PelangganInsertActivity::class.java)
                        intent.putExtra("tag",pelanggan.idpelanggan)
                        intent.putExtra("type","Update")
                        startActivity(intent)
                        finish()
                    })
                    recListPelanggan.adapter = adapter
                    val layoutmanager = LinearLayoutManager(this@PelangganListActivity)
                    recListPelanggan.layoutManager = layoutmanager
                    recListPelanggan.setHasFixedSize(true)
                    val jumlahData = findViewById<TextView>(R.id.tJumlahPelanggan)
                    jumlahData.text = "Jumlah Data : ${response.body().size}"
                }else{
                    Toast.makeText(this@PelangganListActivity,"Tidak Ada Data", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Pelanggan>>?, t: Throwable?) {
                hideProgressBar()
                val alert = AlertDialog.Builder(this@PelangganListActivity)
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

    private fun deletePelanggan(pelanggan: Pelanggan) {
        val request = Function().builder()
        val token = Function().token(this)
        val alert = AlertDialog.Builder(this@PelangganListActivity)
        alert.setTitle("DELETE")
        alert.setMessage("Are You Sure?")
        alert.setPositiveButton("YES",{dialog, which ->
            showProgressBar()
            request.deletePelanggan(pelanggan.idpelanggan,token).enqueue(object : retrofit2.Callback<Pelanggan> {
                override fun onResponse(call: Call<Pelanggan>?, response: Response<Pelanggan>?) {
                    hideProgressBar()
                    if(response!!.isSuccessful){
                        if(response.body().status == "true"){
                            Toast.makeText(this@PelangganListActivity,"Berhasil Delete",Toast.LENGTH_LONG).show()
                            loadData()
                        }else{
                            Toast.makeText(this@PelangganListActivity,"Data masih digunakan ditempat lain",Toast.LENGTH_LONG).show()
                        }

                    }else{
                        Toast.makeText(this@PelangganListActivity,"Gagal Delete",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Pelanggan>?, t: Throwable?) {
                    hideProgressBar()
                    val alert = AlertDialog.Builder(this@PelangganListActivity)
                    alert.setTitle("ERROR")
                    alert.setMessage("terjadi error, coba lagi")
                    alert.setPositiveButton("RETRY",{dialog, which ->
                        deletePelanggan(pelanggan)
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
        progressPelanggan.visibility =  View.VISIBLE
        recListPelanggan.visibility = View.INVISIBLE
    }

    fun hideProgressBar(){
        progressPelanggan.visibility = View.INVISIBLE
        recListPelanggan.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        val intent = Intent(this,MasterActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}
