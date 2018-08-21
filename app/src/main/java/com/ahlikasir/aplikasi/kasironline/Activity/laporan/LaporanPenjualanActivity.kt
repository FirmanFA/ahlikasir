package com.ahlikasir.aplikasi.kasironline.Activity.laporan

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.adapter.laporan.LaporanPendapatanAdapter
import com.ahlikasir.aplikasi.kasironline.model.transaksi.Penjualan
import com.github.mikephil.charting.charts.LineChart
import kotlinx.android.synthetic.main.activity_laporan_penjualan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class LaporanPenjualanActivity : AppCompatActivity() {

    private lateinit var adapter: LaporanPendapatanAdapter
    private lateinit var calendar: Calendar
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan_penjualan)

        calendar = Calendar.getInstance()
//        search()
        setFirstDate()
        loadData()
        setTglAwalPenjualan()
        setTglAkhirPenjualan()

        bayar.setOnClickListener {

            if (count >0){
                val intent = Intent (this@LaporanPenjualanActivity,ExportExcelActivity::class.java)
                intent.putExtra("exportType","penjualan")
                intent.putExtra("tglawal",tTglAwalPenjualan.text.toString())
                intent.putExtra("tglakhir",tTglAkhirPenjualan.text.toString())
                startActivity(intent)
            }else{
                Function().toast("Tidak Ada Data",this@LaporanPenjualanActivity)
            }
        }

    }

//    private fun search(){
//        eCari.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                adapter.filter.filter(eCari.text.toString())
//                tJumlah.text = "Jumlah Data : " + adapter.itemCount
//            }
//        })
//    }

    fun loadPendapatan(){
        val request = Function().builder()
        val token = Function().token(this)
        val emailraw = Function().email(this)
        val email = URLEncoder.encode(emailraw,"UTF-8")

        request.getPendapatan(email,tTglAwalPenjualan.text.toString(),tTglAkhirPenjualan.text.toString(),token).enqueue(object : Callback<Penjualan>{
            override fun onResponse(call: Call<Penjualan>?, response: Response<Penjualan>?) {
                if(response!!.isSuccessful){
                    val bayar = NumberFormat.getNumberInstance(Locale.US).format(response.body().bayar.toLong()).toString()

                    val bayarr = "Rp. " + bayar

                    tTotal.text = bayarr

                }
            }

            override fun onFailure(call: Call<Penjualan>?, t: Throwable?) {

            }
        })

    }

    private fun loadData() {
        val request = Function().builder()
        val token = Function().token(this)
        val emailraw = Function().email(this)
        val email = URLEncoder.encode(emailraw,"UTF-8")
        request.getPenjualan(email,tTglAwalPenjualan.text.toString(),tTglAkhirPenjualan.text.toString(),token).enqueue(object : Callback<List<Penjualan>> {
            override fun onResponse(call: Call<List<Penjualan>>?, response: Response<List<Penjualan>>?) {

                if(response!!.isSuccessful){
                    if(response.body()[0].status == "false"){
                        Function().toast("Tidak Ada Data",this@LaporanPenjualanActivity)
                    }else{
                        adapter = LaporanPendapatanAdapter(this@LaporanPenjualanActivity,response.body(),{penjualan ->
                            deletePenjualan(penjualan.idjual)
                        })
                        recLapPenjualan.adapter = adapter
                        val layoutmanager = LinearLayoutManager(this@LaporanPenjualanActivity)
                        recLapPenjualan.layoutManager = layoutmanager
                        recLapPenjualan.setHasFixedSize(true)
                        adapter.notifyDataSetChanged()
                        val jumlahData = "Jumlah Data : " + adapter.itemCount
                        tJumlah.text = jumlahData
                        loadPendapatan()
                        count = adapter.itemCount

                    }
                }
            }

            override fun onFailure(call: Call<List<Penjualan>>?, t: Throwable?) {
                val alert = AlertDialog.Builder(this@LaporanPenjualanActivity)
                alert.setTitle("ERROR")
                alert.setMessage("terjadi error, coba lagi")
                alert.setPositiveButton("RETRY",{ dialog, _ ->
                    loadData()
                    dialog.dismiss()
                })
                alert.show()
            }
        })
    }

    private fun setTglAwalPenjualan(){
        tTglAwalPenjualan.setOnClickListener {
            DatePickerDialog(this@LaporanPenjualanActivity, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR,year)
                calendar.set(Calendar.MONTH,month)
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                updateTglAwal()
                loadData()
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun updateTglAwal() {
        val format = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(format, Locale.US)

        val tglRaw = sdf.format(calendar.time)

        val tgl = tglRaw.replace("/","-")

        tTglAwalPenjualan.text = tgl

    }

    private fun setTglAkhirPenjualan(){
        tTglAkhirPenjualan.setOnClickListener {
            DatePickerDialog(this@LaporanPenjualanActivity, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR,year)
                calendar.set(Calendar.MONTH,month)
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                updateTglAkhir()
                loadData()
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun updateTglAkhir() {
        val format = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(format, Locale.US)

        val tglRaw = sdf.format(calendar.time)

        val tgl = tglRaw.replace("/","-")

        tTglAkhirPenjualan.text = tgl
    }

    private fun setFirstDate(){
        val sdf = SimpleDateFormat("yyyy-MM-dd",Locale.US)

        tTglAwalPenjualan.text = sdf.format(Date())
        tTglAkhirPenjualan.text = sdf.format(Date())

    }

    private fun deletePenjualan(idjual:String) {

    }

}
