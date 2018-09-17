package com.ahlikasir.aplikasi.kasironline.Activity.laporan

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import com.ahlikasir.aplikasi.kasironline.Activity.transaksi.penjualan.CetakCariActivity
import com.ahlikasir.aplikasi.kasironline.Activity.transaksi.penjualan.PenjualanCetakActivity
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.adapter.laporan.LaporanPendapatanAdapter
import com.ahlikasir.aplikasi.kasironline.model.transaksi.Penjualan
import kotlinx.android.synthetic.main.activity_laporan_pendapatan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class LaporanPendapatanActivity : AppCompatActivity() {

    lateinit var calendar:Calendar
    lateinit var adapter:LaporanPendapatanAdapter
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan_pendapatan)

        calendar = Calendar.getInstance()

        setFirstDate()
        loadData()
        setTglAwal()
        setTglAkhir()

        bayar.setOnClickListener {
            if (count >0){
                val intent = Intent (this@LaporanPendapatanActivity,ExportExcelActivity::class.java)
                intent.putExtra("exportType","pendapatan")
                intent.putExtra("tglawal",tTglAwal.text.toString())
                intent.putExtra("tglakhir",tTglAkhir.text.toString())
                startActivity(intent)
            }else{
                Function().toast("Tidak Ada Data",this@LaporanPendapatanActivity)
            }
        }

    }

    fun setTglAwal(){
        tTglAwal.setOnClickListener {
            DatePickerDialog(this@LaporanPendapatanActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR,year)
                calendar.set(Calendar.MONTH,month)
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                updateTglAwal()
                loadData()
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun loadData(){
        val request = Function().builder()
        val token = Function().token(this)
        val emailraw = Function().email(this)
        val email = URLEncoder.encode(emailraw,"UTF-8")
        request.getPenjualan(email,tTglAwal.text.toString(),tTglAkhir.text.toString(),token).enqueue(object : Callback<List<Penjualan>> {
            override fun onResponse(call: Call<List<Penjualan>>?, response: Response<List<Penjualan>>?) {

                if(response!!.isSuccessful){
                    if(response.body()[0].status == "false"){
                        Function().toast("Tidak Ada Data",this@LaporanPendapatanActivity)
                    }else{
                        adapter = LaporanPendapatanAdapter(this@LaporanPendapatanActivity,response.body(),{penjualan ->
                            val intent = Intent(this@LaporanPendapatanActivity,PenjualanCetakActivity::class.java)
                            Function().setSharedPrefrences("strukFaktur",penjualan.faktur,this@LaporanPendapatanActivity)
                            Function().setSharedPrefrences("strukTanggal",penjualan.tgljual,this@LaporanPendapatanActivity)
                            Function().setSharedPrefrences("strukPelanggan",penjualan.pelanggan,this@LaporanPendapatanActivity)
                            Function().setSharedPrefrences("strukBayar",penjualan.bayar,this@LaporanPendapatanActivity)
                            Function().setSharedPrefrences("strukTotal",penjualan.total,this@LaporanPendapatanActivity)
                            Function().setSharedPrefrences("strukKembali",penjualan.kembali,this@LaporanPendapatanActivity)
                            Function().setSharedPrefrences(CetakCariActivity.EXTRA_ADDRESS,"Tidak Ada Perangkat",this@LaporanPendapatanActivity)
                            Function().setSharedPrefrences(CetakCariActivity.EXTRA_NAME,"Tidak Ada Perangkat",this@LaporanPendapatanActivity)
                            startActivity(intent)
                        },{penjualan ->
                            deletePenjualan(penjualan.idjual)
                        })
                        recLapPenjualan.adapter = adapter
                        val layoutmanager = LinearLayoutManager(this@LaporanPendapatanActivity)
                        recLapPenjualan.layoutManager = layoutmanager
                        recLapPenjualan.setHasFixedSize(true)
                        adapter.notifyDataSetChanged()
                        val jumlahData = "Jumlah Data : " + adapter.itemCount
                        tJumlahData.text = jumlahData
                        loadPendapatan()
                        count = adapter.itemCount

                    }
                }
            }

            override fun onFailure(call: Call<List<Penjualan>>?, t: Throwable?) {
                val alert = AlertDialog.Builder(this@LaporanPendapatanActivity)
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

    private fun loadPendapatan() {
        val request = Function().builder()
        val token = Function().token(this)
        val emailraw = Function().email(this)
        val email = URLEncoder.encode(emailraw,"UTF-8")

        request.getPendapatan(email,tTglAwal.text.toString(),tTglAkhir.text.toString(),token).enqueue(object : Callback<Penjualan>{
            override fun onResponse(call: Call<Penjualan>?, response: Response<Penjualan>?) {
                if(response!!.isSuccessful){
                    val pendapatan = NumberFormat.getNumberInstance(Locale.US).format(response.body().pendapatan.toLong()).toString()
                    val bayar = NumberFormat.getNumberInstance(Locale.US).format(response.body().bayar.toLong()).toString()
                    val kembali = NumberFormat.getNumberInstance(Locale.US).format(response.body().kembali.toLong()).toString()

                    tCaption.text = "Total Pendapatan :"
                    tValue.text = "Rp. "+pendapatan
                    val value2 = "Total Pembayaran :"+ "\n"+ "Rp. " +
                            bayar + "\n" +
                            "Total Kembalian :" + "\n" + "Rp. " +
                            kembali
                    tValue2.text = value2

                }
            }

            override fun onFailure(call: Call<Penjualan>?, t: Throwable?) {

            }
        })
    }

    private fun deletePenjualan(idjual: String?) {
        val request = Function().builder()
        val token = Function().token(this)
        request.deletePenjualan(idjual,token).enqueue(object : Callback<Penjualan>{
            override fun onFailure(call: Call<Penjualan>?, t: Throwable?) {
                Function().toast("Error" + t?.message,this@LaporanPendapatanActivity)
            }

            override fun onResponse(call: Call<Penjualan>?, response: Response<Penjualan>?) {
                loadData()
            }

        })
    }

    private fun updateTglAwal() {
        val format = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(format, Locale.US)

        val tglRaw = sdf.format(calendar.time)

        val tgl = tglRaw.replace("/","-")

        tTglAwal.text = tgl

    }

    fun setTglAkhir(){
        tTglAkhir.setOnClickListener {
            DatePickerDialog(this@LaporanPendapatanActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
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

        tTglAkhir.text = tgl
    }

    fun setFirstDate(){
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        tTglAwal.text = sdf.format(Date())
        tTglAkhir.text = sdf.format(Date())

    }

}
