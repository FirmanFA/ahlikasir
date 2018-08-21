package com.ahlikasir.aplikasi.kasironline.Activity.master.barang

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.adapter.barang.BarangAdapter
import com.ahlikasir.aplikasi.kasironline.Activity.master.MasterActivity
import com.ahlikasir.aplikasi.kasironline.model.barang.Barang
import com.ahlikasir.aplikasi.kasironline.model.kelompok.Kelompok
import kotlinx.android.synthetic.main.activity_barang_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class BarangListActivity : AppCompatActivity() {

    lateinit var adapter:BarangAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barang_list)

        hideProgressBar()

        val jumlahdata = findViewById<TextView>(R.id.tJumlahbarang)

        loadData()
        loadSpinner()
        spinnerSelect()


        tambahBarang.setOnClickListener {
            val intent = Intent(this,BarangInsertActivity::class.java)
            intent.putExtra("type","Insert")
            startActivity(intent)
            finish()
        }

        eCari.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(eCari.text.toString())
                jumlahdata.text = "Jumlah Data : ${adapter.itemCount}"
            }
        })

    }

    private fun spinnerSelect() {
        var selectedKelompok: String
        sKategori.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedKelompok = parent?.getItemAtPosition(position).toString()
                if(selectedKelompok == "semua"){
                    loadData()
                }else{
                    selectWhere(selectedKelompok)
                }
            }
        }
    }

    fun selectWhere(kelompok:String){
        showProgressBar()
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        request.getIdKelompok(kelompok,encodeEmail,token).enqueue(object: Callback<Kelompok>{
            override fun onFailure(call: Call<Kelompok>?, t: Throwable?) {
                hideProgressBar()
                val alert = AlertDialog.Builder(this@BarangListActivity)
                alert.setTitle("ERROR")
                alert.setMessage("terjadi error, coba lagi")
                alert.setPositiveButton("RETRY",{dialog, which ->
                    selectWhere(kelompok)
                    dialog.dismiss()
                })
                alert.show()
            }

            override fun onResponse(call: Call<Kelompok>?, response: Response<Kelompok>?) {
                request.getBarangByKelompok(encodeEmail,response!!.body().idkelompok,token).enqueue(object : Callback<List<Barang>>{
                    override fun onFailure(call: Call<List<Barang>>?, t: Throwable?) {
                        hideProgressBar()
                    }

                    override fun onResponse(call: Call<List<Barang>>?, response: Response<List<Barang>>?) {
                        hideProgressBar()
                        if (response!!.isSuccessful){
                            adapter = BarangAdapter(this@BarangListActivity,response.body(),{barang ->
                                deleteClick(barang)
                            },{barang ->
                                updateClick(barang)
                            })
                            recListbarang.adapter = adapter
                            val layoutmanager = LinearLayoutManager(this@BarangListActivity)
                            recListbarang.layoutManager = layoutmanager
                            recListbarang.setHasFixedSize(true)
                            val jumlahdata = findViewById<TextView>(R.id.tJumlahbarang)
                            jumlahdata.text = "Jumlah Data : ${response.body().size}"
                        }else{
                            Toast.makeText(this@BarangListActivity,"Tidak Ada data",Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
        })
    }

    private fun loadSpinner() {
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        request.getKelompok(encodeEmail,token).enqueue(object: Callback<List<Kelompok>>{
            override fun onFailure(call: Call<List<Kelompok>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<List<Kelompok>>?, response: Response<List<Kelompok>>?) {
                if (response!!.isSuccessful){
                    val listForSpinner = ArrayList<String>()

                    listForSpinner.add("semua")
                    for(i in 0 until response!!.body().size){
                        listForSpinner.add(response.body()[i].kelompok)
                    }
                    //set result json ke adapter spinner
                    val adapter = ArrayAdapter<String>(this@BarangListActivity,android.R.layout.simple_spinner_item,listForSpinner)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    sKategori.adapter = adapter
                }
            }
        })


    }

    private fun loadData() {
        showProgressBar()
        val jumlahdata = findViewById<TextView>(R.id.tJumlahbarang)
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        request.getBarang(encodeEmail,token).enqueue(object : retrofit2.Callback<List<Barang>>{
            override fun onResponse(call: Call<List<Barang>>?, response: Response<List<Barang>>?) {
                hideProgressBar()
                if (response!!.isSuccessful){
                    adapter = BarangAdapter(this@BarangListActivity,response.body(),{barang ->

                        deleteClick(barang)
                        adapter.notifyDataSetChanged()

                    },{barang ->
                        updateClick(barang)
                    })
                    recListbarang.adapter = adapter
                    val layoutmanager = LinearLayoutManager(this@BarangListActivity)
                    recListbarang.layoutManager = layoutmanager
                    recListbarang.setHasFixedSize(true)
                    jumlahdata.text = "Jumlah Data : ${response.body().size}"
                }else{
                    Toast.makeText(this@BarangListActivity,"Tidak Ada data",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Barang>>?, t: Throwable?) {
                hideProgressBar()
                val alert = AlertDialog.Builder(this@BarangListActivity)
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

    fun updateClick(barang: Barang){
        val intent = Intent(this@BarangListActivity, BarangInsertActivity::class.java)
        intent.putExtra("tag",barang.idbarang)
        intent.putExtra("type","Update")
        startActivity(intent)
        finish()
    }

    fun deleteClick(barang: Barang){
        val request = Function().builder()
        val token = Function().token(this)
        val alert = AlertDialog.Builder(this@BarangListActivity)
        alert.setTitle("DELETE")
        alert.setMessage("Are You Sure?")
        alert.setPositiveButton("YES",{dialog, which ->
            showProgressBar()
            request.deleteBarang(barang.idbarang,token).enqueue(object: Callback<Barang>{
                override fun onResponse(call: Call<Barang>?, response: Response<Barang>?) {
                    hideProgressBar()

                    if(response!!.body().status == "true"){
                        Toast.makeText(this@BarangListActivity,"Berhasil Delete",Toast.LENGTH_LONG).show()
                        loadData()
                    }else{
                        Toast.makeText(this@BarangListActivity,"Data masih digunakan ditempat lain",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Barang>?, t: Throwable?) {
                    hideProgressBar()
                    val alert = AlertDialog.Builder(this@BarangListActivity)
                    alert.setTitle("ERROR")
                    alert.setMessage("terjadi error, coba lagi")
                    alert.setPositiveButton("RETRY",{dialog, which ->
                        deleteClick(barang)
                        dialog.dismiss()
                    })
                    alert.show()
                }
            })
        })
        alert.setNegativeButton("NO",{dialog, which ->
            dialog.dismiss()
        })
        alert.show()
    }

    fun showProgressBar(){
        progressBarang.visibility =  View.VISIBLE
        recListbarang.visibility = View.INVISIBLE
    }

    fun hideProgressBar(){
        progressBarang.visibility = View.INVISIBLE
        recListbarang.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        val intent = Intent(this,MasterActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}
