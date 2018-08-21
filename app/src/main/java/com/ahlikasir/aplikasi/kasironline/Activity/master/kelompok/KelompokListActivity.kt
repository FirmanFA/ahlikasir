package com.ahlikasir.aplikasi.kasironline.Activity.master.kelompok

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
import com.ahlikasir.aplikasi.kasironline.adapter.kelompok.KelompokAdapter
import com.ahlikasir.aplikasi.kasironline.Activity.master.MasterActivity
import com.ahlikasir.aplikasi.kasironline.model.kelompok.Kelompok
import kotlinx.android.synthetic.main.activity_kelompok_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class KelompokListActivity : AppCompatActivity() {

    lateinit var adapter:KelompokAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kelompok_list)

        hideProgressBar()

        val jumlahData = findViewById<TextView>(R.id.tJumlahKel)

        jumlahData.text = "Jumlah Data : 0"

        loadData()

        insertKelompokBtn.setOnClickListener {
            val intent = Intent(this, KelInsertActivity::class.java)
            intent.putExtra("type","Insert")
            startActivity(intent)
            finish()
        }

        val cari = findViewById<EditText>(R.id.eCariKel)
        cari.addTextChangedListener(object: TextWatcher{
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

    fun loadData(){
        showProgressBar()
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        request.getKelompok(encodeEmail,token).enqueue(object : Callback<List<Kelompok>> {
            override fun onResponse(call: Call<List<Kelompok>>?, response: Response<List<Kelompok>>?) {
                hideProgressBar()
                if (response!!.isSuccessful) {
                    adapter = KelompokAdapter(this@KelompokListActivity,response.body(),{kelompok ->


                        deleteKel(kelompok)
                        adapter.notifyDataSetChanged()
//                        respon()
                        loadData()

                    },{kelompok ->
                        val intent = Intent(this@KelompokListActivity,KelInsertActivity::class.java)
                        intent.putExtra("tag",kelompok.idkelompok)
                        intent.putExtra("type","Update")
                        startActivity(intent)
                        finish()
                    })
                    recyclerKelompok.adapter = adapter
                    val layoutmanager = LinearLayoutManager(this@KelompokListActivity)
                    recyclerKelompok.layoutManager = layoutmanager
                    recyclerKelompok.setHasFixedSize(true)
                    val jumlahData = findViewById<TextView>(R.id.tJumlahKel)
                    jumlahData.text = "Jumlah Data : ${response.body().size}"
                }else{
                    Toast.makeText(this@KelompokListActivity,"tidak ada data",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Kelompok>>?, t: Throwable?) {
                hideProgressBar()
                val alert = AlertDialog.Builder(this@KelompokListActivity)
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

    fun deleteKel(kelompok: Kelompok){
        val request = Function().builder()
        val token = Function().token(this)
        val alert = AlertDialog.Builder(this@KelompokListActivity)
        alert.setTitle("DELETE")
        alert.setMessage("Are You Sure?")
        alert.setPositiveButton("YES",{dialog, _ ->
            showProgressBar()
            request.deleteKelompok(kelompok.idkelompok,token).enqueue(object : Callback<Kelompok> {
                override fun onResponse(call: Call<Kelompok>?, response: Response<Kelompok>?) {
                    hideProgressBar()
                    if(response!!.isSuccessful){
                        if(response.body().status == "true"){
                            Toast.makeText(this@KelompokListActivity,"Berhasil Delete",Toast.LENGTH_LONG).show()
                            loadData()
                        }else{
                            Toast.makeText(this@KelompokListActivity,"Data masih digunakan ditempat lain",Toast.LENGTH_LONG).show()
                        }

                    }else{
                        Toast.makeText(this@KelompokListActivity,"Gagal Delete",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Kelompok>?, t: Throwable?) {
                    hideProgressBar()
                    val alert = AlertDialog.Builder(this@KelompokListActivity)
                    alert.setTitle("ERROR")
                    alert.setMessage("terjadi error, coba lagi")
                    alert.setPositiveButton("RETRY",{dialog, which ->
                        deleteKel(kelompok)
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
        progresssKelompok.visibility =  View.VISIBLE
        recyclerKelompok.visibility = View.INVISIBLE
    }

    fun hideProgressBar(){
        progresssKelompok.visibility = View.INVISIBLE
        recyclerKelompok.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        val intent = Intent(this,MasterActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}
