package com.ahlikasir.aplikasi.kasironline.Activity.master.barang

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.model.barang.Barang
import com.ahlikasir.aplikasi.kasironline.model.kelompok.Kelompok
import com.ahlikasir.aplikasi.kasironline.model.satuan.Satuan
import kotlinx.android.synthetic.main.activity_barang_insert.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.text.DecimalFormat

class BarangInsertActivity : AppCompatActivity() {

    var idSatuan = ""
    var idKelompok = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barang_insert)

        val type = intent.getStringExtra("type")
        this.title = "$type Barang"

        loadSpinner()

        etNamaBarang.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val limit = findViewById<TextView>(R.id.maxNamaBarang)
                limit.text = "${etNamaBarang.length()}/50"
                if(etNamaBarang.length() == 50 || etNamaBarang.length() == 0){
                    limit.setTextColor(Color.RED)
                }else{
                    limit.setTextColor(Color.BLACK)
                }
            }

        })

        showText()


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
                if(response!!.isSuccessful){
                    val listForSpinner = ArrayList<String>()

                    for(i in 0 until response!!.body().size){
                        listForSpinner.add(response.body()[i].kelompok)
                    }
                    //set result json ke adapter spinner

                    val adapter = ArrayAdapter<String>(this@BarangInsertActivity,android.R.layout.simple_spinner_item,listForSpinner)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinKelompok.adapter = adapter
                }
            }
        })

        request.getSatuan(encodeEmail,token).enqueue(object: Callback<List<Satuan>>{
            override fun onFailure(call: Call<List<Satuan>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<List<Satuan>>?, response: Response<List<Satuan>>?) {
                if(response!!.isSuccessful){
                    val listForSpinner = ArrayList<String>()

                    for(i in 0 until response!!.body().size){
                        listForSpinner.add(response.body()[i].satuan1)
                    }
                    //set result json ke adapter spinner

                    val adapter = ArrayAdapter<String>(this@BarangInsertActivity,android.R.layout.simple_spinner_item,listForSpinner)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinSatuan.adapter = adapter
                }
            }
        })


    }



    fun insertUpdate(v:View){
        val id = intent.getStringExtra("tag")
        val selectedSatuan = spinSatuan.selectedItem.toString()
//        val splitSatuan = selectedSatuan.split("\\s*-\\s*")
//        val satuan = splitSatuan[0]
        val kelompok = spinKelompok.selectedItem.toString()
        val barang = etNamaBarang.text.toString()
        val harga1 = etHarga1.text.toString()
        val harga2 = etHarga2.text.toString()
        val stok = etStok.text.toString()
        val request = Function().builder()
        val token = Function().token(this)
        val type = intent.getStringExtra("type")
        val email = Function().email(this)
        val isiBarang = Barang(selectedSatuan,kelompok,barang,harga1,harga2,stok,email)

        if (barang == "" || harga1 == "" || harga2 == "" || stok == ""){
            Toast.makeText(this@BarangInsertActivity,"ada data yang kosong", Toast.LENGTH_LONG).show()
        }else{
            if(type.equals("Insert")){
                request.insertBarang(isiBarang,token).enqueue(object: Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                        if (response!!.isSuccessful){
                            val intent = Intent(this@BarangInsertActivity,BarangListActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this@BarangInsertActivity,"gagal insert coba lagi", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        val alert = AlertDialog.Builder(this@BarangInsertActivity)
                        alert.setTitle("ERROR")
                        alert.setMessage("terjadi error, coba lagi")
                        alert.setPositiveButton("RETRY",{dialog, which ->
                            insertUpdate(v)
                            dialog.dismiss()
                        })
                        alert.show()
                    }
                })
            }else{
                request.updateBarang(id,isiBarang,token).enqueue(object: Callback<Barang>{
                    override fun onResponse(call: Call<Barang>?, response: Response<Barang>?) {
                        if(response!!.isSuccessful){
                            if(response.body().status == "true"){
                                val intent = Intent(this@BarangInsertActivity,BarangListActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(this@BarangInsertActivity,"Data masih digunakan ditempat lain",Toast.LENGTH_LONG).show()
                            }

                        }else{
                            Toast.makeText(this@BarangInsertActivity,"Gagal Update",Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Barang>?, t: Throwable?) {
                        val alert = AlertDialog.Builder(this@BarangInsertActivity)
                        alert.setTitle("ERROR")
                        alert.setMessage("terjadi error, coba lagi")
                        alert.setPositiveButton("RETRY",{dialog, which ->
                            insertUpdate(v)
                            dialog.dismiss()
                        })
                        alert.show()
                    }
                })
            }
        }


    }

    fun showText(){

        val id = intent.getStringExtra("tag")
        val type = intent.getStringExtra("type")
        val request = Function().builder()
        val token = Function().token(this)
        val df = DecimalFormat("#.##")
        val email = Function().email(this)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        if(type.equals("Insert")){
            val string = "bla"
        }else{
            request.getBarangById(encodeEmail,id,token).enqueue(object: Callback<Barang> {
                override fun onResponse(call: Call<Barang>?, response: Response<Barang>?) {
                    val final = df.format(response!!.body().stok.toDouble())
                    etNamaBarang.setText(response.body().barang)
                    etHarga1.setText(response.body().hargasatuan1)
                    etHarga2.setText(response.body().hargasatuan2)
                    etStok.setText(final)
                }

                override fun onFailure(call: Call<Barang>?, t: Throwable?) {
                    val alert = AlertDialog.Builder(this@BarangInsertActivity)
                    alert.setTitle("ERROR")
                    alert.setMessage("terjadi error, coba lagi")
                    alert.setPositiveButton("RETRY",{dialog, which ->
                        showText()
                        dialog.dismiss()
                    })
                    alert.show()
                }
            })
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this,BarangListActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}
