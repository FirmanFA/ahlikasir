package com.ahlikasir.aplikasi.kasironline.Activity.laporan

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.adapter.laporan.LaporanPendapatanAdapter
import com.ahlikasir.aplikasi.kasironline.model.transaksi.Penjualan
import kotlinx.android.synthetic.main.activity_laporan_pendapatan.*
import kotlinx.android.synthetic.main.activity_pembayaran_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

class LaporanPerfakturActivity : AppCompatActivity() {

    lateinit var adapter: LaporanPendapatanAdapter
    lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan_perfaktur)

        calendar = Calendar.getInstance()
        setFirstDate()
        loadData()
        setTglAwal()
        setTglAkhir()

    }

    private fun loadData() {
        val request = Function().builder()
        val token = Function().token(this)
        val emailraw = Function().email(this)
        val email = URLEncoder.encode(emailraw,"UTF-8")
        request.getPenjualan(email,tTglAwal.text.toString(),tTglAkhir.text.toString(),token).enqueue(object : Callback<List<Penjualan>> {
            override fun onResponse(call: Call<List<Penjualan>>?, response: Response<List<Penjualan>>?) {

                if(response!!.isSuccessful){
                    adapter = LaporanPendapatanAdapter(this@LaporanPerfakturActivity,response.body(),{penjualan ->
                        deletePenjualan(penjualan.idjual)
                    })
                    bayarRecycler.adapter = adapter
                    val layoutmanager = LinearLayoutManager(this@LaporanPerfakturActivity)
                    bayarRecycler.layoutManager = layoutmanager
                    bayarRecycler.setHasFixedSize(true)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Penjualan>>?, t: Throwable?) {
                val alert = AlertDialog.Builder(this@LaporanPerfakturActivity)
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

    fun setTglAwal(){
        tTglAwal.setOnClickListener {
            DatePickerDialog(this@LaporanPerfakturActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
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

        tTglAwal.setText(tgl)

    }

    fun setTglAkhir(){
        tTglAkhir.setOnClickListener {
            DatePickerDialog(this@LaporanPerfakturActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
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

    private fun deletePenjualan(idjual: String) {

    }
}
