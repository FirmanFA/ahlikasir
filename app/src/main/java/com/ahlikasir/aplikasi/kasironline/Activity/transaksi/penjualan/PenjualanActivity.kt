package com.ahlikasir.aplikasi.kasironline.Activity.transaksi.penjualan

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.adapter.transaksi.PenjualanAdapter
import com.ahlikasir.aplikasi.kasironline.model.barang.Barang
import com.ahlikasir.aplikasi.kasironline.model.login.Login
import com.ahlikasir.aplikasi.kasironline.model.satuan.Satuan
import com.ahlikasir.aplikasi.kasironline.model.transaksi.Penjualan
import kotlinx.android.synthetic.main.activity_penjualan.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PenjualanActivity : AppCompatActivity(),DatePickerDialog.OnDateSetListener {

    private lateinit var calendar:Calendar
    lateinit var adapter:PenjualanAdapter

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(Calendar.YEAR,year)
        calendar.set(Calendar.MONTH,month)
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
        updateLabel()
    }

    private fun updateLabel() {
        val format = "yyyy/MM/dd"
        val sdf = SimpleDateFormat(format, Locale.US)

        tanggal.setText(sdf.format(calendar.time))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penjualan)

        eventHandler()
        val faktur = Function().getShared("fakturBayar","",this)
        if (faktur == ""){
            loadFaktur()
        }else{
            noFaktur.setText(faktur)
            loadData()
        }
        btn2.setOnClickListener{
            simpanClick()
        }
    }

    private fun loadFaktur(){
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        request.getFaktur(encodeEmail,token).enqueue(object: Callback<Login>{
            override fun onResponse(call: Call<Login>?, response: Response<Login>?) {
                noFaktur.setText(response!!.body().status)
                loadData()
            }

            override fun onFailure(call: Call<Login>?, t: Throwable?) {

            }
        })
    }

    private fun simpanClick(){
        loadData()
        val faktur = findViewById<EditText>(R.id.noFaktur)
        val tanggal = findViewById<EditText>(R.id.tanggal)
        val harga = findViewById<EditText>(R.id.harga)
        val jumlah = findViewById<EditText>(R.id.jumlah)
        val keterangan = findViewById<EditText>(R.id.etKeteranganJual)
        val idbarang = Function().getShared("idbarang","",this)
        val idpelanggan = Function().getShared("idpelanggan","",this)
        val indexSatuan = spinnerJualSatuan.selectedItemPosition
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)

        if(noFaktur.text.toString() == "" || barang.text.toString() == "kosong" || harga.text.toString() == ""){
            Toast.makeText(this,"Ada Data Yang Kosong",Toast.LENGTH_LONG).show()
        }else if(pelangganJual.text.toString() == "kosong"){
            val satuanJual = spinnerJualSatuan.selectedItem.toString()
            val hargaJual = (harga.text.toString().toInt() * jumlah.text.toString().toDouble()).toString()
            val isiPenjualan = Penjualan(idbarang,jumlah.text.toString(),hargaJual,keterangan.text.toString(),satuanJual,
                    "",faktur.text.toString(),tanggal.text.toString(),"0","0","0",email,indexSatuan.toString())
            request.penjualan(isiPenjualan,token).enqueue(object: Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    val alert = AlertDialog.Builder(this@PenjualanActivity)
                    alert.setTitle("ERROR")
                    alert.setMessage("terjadi error, coba lagi")
                    alert.setPositiveButton("RETRY",{ dialog, _ ->
                        simpanClick()
                        dialog.dismiss()
                    })
                    alert.show()
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    loadData()
                    jumlah.setText("1")

                }
            })
        }else{
            val satuanJual = spinnerJualSatuan.selectedItem.toString()
            val hargaJual = (harga.text.toString().toInt() * jumlah.text.toString().toDouble()).toString()
            val isiPenjualan = Penjualan(idbarang,jumlah.text.toString(),hargaJual,keterangan.text.toString(),satuanJual,
                    idpelanggan,faktur.text.toString(),tanggal.text.toString(),"0","0","0",email,indexSatuan.toString())
            request.penjualan(isiPenjualan,token).enqueue(object: Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    val alert = AlertDialog.Builder(this@PenjualanActivity)
                    alert.setTitle("ERROR")
                    alert.setMessage("terjadi error, coba lagi")
                    alert.setPositiveButton("RETRY",{ dialog, _ ->
                        simpanClick()
                        dialog.dismiss()
                    })
                    alert.show()
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    loadData()
                }
            })
        }
    }

    fun loadData(){
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val faktur = findViewById<EditText>(R.id.noFaktur)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        request.getPejualanByFaktur(encodeEmail,faktur.text.toString(),token).enqueue(object: Callback<List<Penjualan>>{
            override fun onFailure(call: Call<List<Penjualan>>?, t: Throwable?) {
                val alert = AlertDialog.Builder(this@PenjualanActivity)
                alert.setTitle("ERROR")
                alert.setMessage("terjadi error, coba lagi")
                alert.setPositiveButton("RETRY",{dialog, which ->
                    loadData()
                    dialog.dismiss()
                })
                alert.show()
            }

            override fun onResponse(call: Call<List<Penjualan>>?, response: Response<List<Penjualan>>?) {

                if(response!!.isSuccessful){
                    adapter = PenjualanAdapter(this@PenjualanActivity,response.body(),{penjualan ->
                        deletepenjualan(penjualan)
                        adapter.notifyDataSetChanged()
                        loadData()
                    })
                    recPenjualan.adapter = adapter
                    val layoutmanager = LinearLayoutManager(this@PenjualanActivity)
                    recPenjualan.layoutManager = layoutmanager
                    recPenjualan.setHasFixedSize(true)
                    fun setTotal():Int{
                        var total = 0
                        if (adapter.itemCount > 0){
                            for(i in 0 until adapter.itemCount){
                                total += response.body()[i].hargajual.toInt()
                            }
                        }
                        return total
                    }
                    Function().setSharedPrefrences("total",setTotal().toString(),this@PenjualanActivity)
                    this@PenjualanActivity.title = "Total : Rp. " + setTotal().toString()

                }
            }
        })



    }

    private fun deletepenjualan(penjualan: Penjualan) {
        val request = Function().builder()
        val token = Function().token(this)
        val alert = AlertDialog.Builder(this@PenjualanActivity)
        val index = spinnerJualSatuan.selectedItemPosition.toString()
        alert.setTitle("DELETE")
        alert.setMessage("Are You Sure?")
        alert.setPositiveButton("YES",{dialog, _ ->
            request.deleteDetailJual(penjualan.iddetailjual,index,token).enqueue(object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    val alert = AlertDialog.Builder(this@PenjualanActivity)
                    alert.setTitle("ERROR")
                    alert.setMessage("terjadi error, coba lagi")
                    alert.setPositiveButton("RETRY",{dialog, which ->
                        deletepenjualan(penjualan)
                        dialog.dismiss()
                    })
                    alert.show()
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response!!.isSuccessful){
                        Toast.makeText(this@PenjualanActivity,"Delete Berhasil",Toast.LENGTH_LONG).show()
                        loadData()
                    }else{
                        Toast.makeText(this@PenjualanActivity,"Delete failed",Toast.LENGTH_LONG).show()
                    }
                }
            })
            dialog.dismiss()
        })

        alert.setNegativeButton("NO",{dialog, _ ->
            dialog.dismiss()
        })

        alert.show()


    }

    private fun loadSpinner() {
        val request = Function().builder()
        val token = Function().token(this)
        val rawemail = Function().email(this)

        val email = URLEncoder.encode(rawemail,"UTF-8")

        val idbarang = Function().getShared("idbarang","",this)

        if(idbarang != "kosong"){
            request.getSatuanByBarang(idbarang,token).enqueue(object: Callback<Satuan>{
                override fun onFailure(call: Call<Satuan>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<Satuan>?, response: Response<Satuan>?) {
                    if(response!!.isSuccessful){
                        val listForSpinner = ArrayList<String>()

                        listForSpinner.clear()
                        listForSpinner.add(response.body().satuan1)
                        listForSpinner.add(response.body().satuan2)

                        val adapter = ArrayAdapter<String>(this@PenjualanActivity,android.R.layout.simple_spinner_item,listForSpinner)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerJualSatuan.adapter = adapter
                        spinnerJualSatuan.setSelection(1)
                    }
                }
            })
        }

        spinnerJualSatuan.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                request.getBarangById(email,idbarang,token).enqueue(object: Callback<Barang>{
                    override fun onResponse(call: Call<Barang>?, response: Response<Barang>?) {
                        if (position == 0){
                            harga.setText(response!!.body().hargasatuan1)
                        }else{
                            harga.setText(response!!.body().hargasatuan2)
                        }
                    }

                    override fun onFailure(call: Call<Barang>?, t: Throwable?) {

                    }

                })
            }
        }
    }

    private fun eventHandler(){
        var total:String

        val setPelanggan =Function().getShared("pelanggan","",this)
        val setBarang =Function().getShared("barang","",this)
        val pelanggan = findViewById<EditText>(R.id.pelangganJual)

        pelanggan.setText(setPelanggan)
        barang.setText(setBarang)

        val sdf = SimpleDateFormat("yyyy/MM/dd",Locale.US)
        tanggal.setText(sdf.format(Date()))

        calendar = Calendar.getInstance()
        cariTanggal.setOnClickListener { DatePickerDialog(this,this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show() }

        val faktur = intent.getStringExtra("faktur")
        noFaktur.setText(faktur)

        cariPelanggan.setOnClickListener {
            val intent = Intent(this,PenjualanCariActivity::class.java)
            intent.putExtra("cariType","pelanggan")
            Function().setSharedPrefrences("fakturLama",noFaktur.text.toString(),this@PenjualanActivity)
            startActivity(intent)
        }

        cariBarang.setOnClickListener {
            val intent = Intent(this,PenjualanCariActivity::class.java)
            intent.putExtra("cariType","barang")
            Function().setSharedPrefrences("fakturLama",noFaktur.text.toString(),this@PenjualanActivity)
            startActivity(intent)
        }

        btnMin.setOnClickListener {
            if(jumlah.text.toString().toDouble() >= 1 && jumlah.text.toString().toDouble() < 2 || jumlah.text.toString() == ""){
                jumlah.setText("1.0")
            }else{
                val setJumlah = jumlah.text.toString().toDouble() - 1
                val df = DecimalFormat("#.##")
                val final = df.format(setJumlah)
                jumlah.setText(final)
            }
        }

        btnPlus.setOnClickListener{
            val stok = Function().getShared("stok","",this)
            if(stok != ""){
                if(jumlah.text.toString() == ""){
                    jumlah.setText("1.0")
                }else{
                    if(jumlah.text.toString().toDouble() >= stok.toDouble()){
                        jumlah.setText(stok)
                    }else{
                        val setJumlah = jumlah.text.toString().toDouble() + 1
                        val df = DecimalFormat("#.##")
                        val final = df.format(setJumlah)
                        jumlah.setText(final)
                    }
                }
            }else{
                if(jumlah.text.toString() == ""){
                    jumlah.setText("1.0")
                }else{
                    val setJumlah = jumlah.text.toString().toDouble() + 1
                    val df = DecimalFormat("#.##")
                    val final = df.format(setJumlah)
                    jumlah.setText(final)
                }
            }

        }


        bayar.setOnClickListener {

            if(adapter.itemCount <= 0){
                Function().toast("Beli Terlebih Dulu",this)
            }else{
                val intent = Intent(this,PenjualanBayarActivity::class.java)
                intent.putExtra("faktur",noFaktur.text.toString())
                intent.putExtra("totalBayar",(harga.text.toString().toInt().toDouble() * jumlah.text.toString().toDouble()).toString())
                Function().setSharedPrefrences("strukFaktur",noFaktur.text.toString(),this)
                Function().setSharedPrefrences("strukTanggal",tanggal.text.toString(),this)
                Function().setSharedPrefrences("strukPelanggan",pelangganJual.text.toString(),this)
                startActivity(intent)
                finish()
            }


        }

        jumlah.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                total = (harga.text.toString().toInt() * jumlah.text.toString().toDouble()).toString()
            }
        })

        harga.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(harga.text.toString() == ""){
                    total = "0"
                }else{
                    total = (harga.text.toString().toInt() * jumlah.text.toString().toDouble()).toString()
                }

            }
        })


    }

    override fun onResume() {
        eventHandler()
        loadSpinner()
        val faktur = Function().getShared("fakturLama","",this)
        val fakturBayar = Function().getShared("fakturBayar","",this)

        when {
            fakturBayar != "" -> noFaktur.setText(fakturBayar)
            faktur == "" -> loadFaktur()
            else -> noFaktur.setText(faktur)
        }
        super.onResume()
    }

    override fun finish() {
        Function().setSharedPrefrences("fakturBayar","",this)
        super.finish()
    }

}