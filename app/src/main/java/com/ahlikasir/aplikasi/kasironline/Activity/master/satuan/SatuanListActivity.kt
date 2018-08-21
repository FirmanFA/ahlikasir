package com.ahlikasir.aplikasi.kasironline.Activity.master.satuan

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.adapter.satuan.SatuanAdapter
import com.ahlikasir.aplikasi.kasironline.Activity.master.MasterActivity
import com.ahlikasir.aplikasi.kasironline.model.satuan.Satuan
import kotlinx.android.synthetic.main.activity_satuan_list.*
import retrofit2.Call
import retrofit2.Response
import java.net.URLEncoder

class SatuanListActivity : AppCompatActivity() {

    lateinit var adapter: SatuanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_satuan_list)

        hideProgressBar()
        insertClick()

        val jumlahData = findViewById<TextView>(R.id.tJumlahsatuan)
        jumlahData.text = "Jumlah Data : 0"

        loadData()

        val cari = findViewById<EditText>(R.id.eCariSatuan)
        cari.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(cari.text)
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
        request.getSatuan(encodeEmail,token).enqueue(object: retrofit2.Callback<List<Satuan>> {
            override fun onResponse(call: Call<List<Satuan>>?, response: Response<List<Satuan>>?) {
                hideProgressBar()
                if(response!!.isSuccessful){
                    adapter = SatuanAdapter(this@SatuanListActivity,response!!.body(),{satuan ->

                        deleteSatuan(satuan)
                        adapter.notifyDataSetChanged()
                        loadData()

                    },{satuan ->
                        val intent = Intent(this@SatuanListActivity,SautuanInsertActivity::class.java)
                        intent.putExtra("tag",satuan.idsatuan)
                        intent.putExtra("type","Update")
                        startActivity(intent)
                        finish()
                    })
                    recListSatuan.adapter = adapter
                    val layoutmanager = LinearLayoutManager(this@SatuanListActivity)
                    recListSatuan.layoutManager = layoutmanager
                    recListSatuan.setHasFixedSize(true)
                    val jumlahData = findViewById<TextView>(R.id.tJumlahsatuan)
                    jumlahData.text = "Jumlah Data : ${response.body().size}"
                }else{
                    Toast.makeText(this@SatuanListActivity,"Tidak ada data",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Satuan>>?, t: Throwable?) {
                hideProgressBar()
                val alert = AlertDialog.Builder(this@SatuanListActivity)
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

    fun deleteSatuan(satuan: Satuan) {
        val request = Function().builder()
        val token = Function().token(this)
        val alert = AlertDialog.Builder(this@SatuanListActivity)
        alert.setTitle("DELETE")
        alert.setMessage("Are You Sure?")
        alert.setPositiveButton("YES",{dialog, which ->
            showProgressBar()
            request.deleteSatuan(satuan.idsatuan,token).enqueue(object : retrofit2.Callback<Satuan> {
                override fun onResponse(call: Call<Satuan>?, response: Response<Satuan>?) {
                    hideProgressBar()
                    if(response!!.isSuccessful){
                        if(response.body().status == "true"){
                            Toast.makeText(this@SatuanListActivity,"Berhasil Delete",Toast.LENGTH_LONG).show()
                            loadData()
                        }else{
                            Toast.makeText(this@SatuanListActivity,"Data masih digunakan ditempat lain",Toast.LENGTH_LONG).show()
                        }

                    }else{
                        Toast.makeText(this@SatuanListActivity,"Gagal Delete",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Satuan>?, t: Throwable?) {
                    hideProgressBar()
                    val alert = AlertDialog.Builder(this@SatuanListActivity)
                    alert.setTitle("ERROR")
                    alert.setMessage("terjadi error, coba lagi")
                    alert.setPositiveButton("RETRY",{dialog, which ->
                        deleteSatuan(satuan)
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

    fun insertClick(){
        insertSatuanBtn.setOnClickListener {
            val intent = Intent(this@SatuanListActivity, SautuanInsertActivity::class.java)
            intent.putExtra("type","Insert")
            startActivity(intent)
            finish()
        }
    }

    fun showProgressBar(){
        progressSatuan.visibility =  View.VISIBLE
        recListSatuan.visibility = View.INVISIBLE
    }

    fun hideProgressBar(){
        progressSatuan.visibility = View.INVISIBLE
        recListSatuan.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        val intent = Intent(this,MasterActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}
