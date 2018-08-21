package com.ahlikasir.aplikasi.kasironline.Activity.laporan

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.adapter.laporan.LaporanPendapatanAdapter
import com.ahlikasir.aplikasi.kasironline.model.transaksi.Penjualan
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import kotlinx.android.synthetic.main.activity_laporan_pendapatan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder
import java.security.KeyStore
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LaporanPendapatanActivity : AppCompatActivity() {

    lateinit var adapter:LaporanPendapatanAdapter
    lateinit var calendar:Calendar
    lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan_pendapatan)

        this.title = "Laporan Pendapatan"

        chart = findViewById(R.id.chartPenjualan)

        chart.animateX(4000)
        chart.description.text = ""
        chart.setDrawBorders(false)



        calendar = Calendar.getInstance()
        search()
        setFirstDate()
        loadData()
        setTglAwal()
        setTglAkhir()

    }

    private fun loadChart(){
        val request = Function().builder()
        val token = Function().token(this)
        val emailraw = Function().email(this)
        val email = URLEncoder.encode(emailraw,"UTF-8")
        request.lapPendapatan(tTglAwal.text.toString(),tTglAkhir.text.toString(),email,token).enqueue(object : Callback<List<Penjualan>> {
            override fun onFailure(call: Call<List<Penjualan>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<List<Penjualan>>?, response: Response<List<Penjualan>>?) {
                if(response!!.isSuccessful){
                    val pendapatan = Array(response.body().size,{i -> response.body()[i].pendapatan.toInt() })
                    setData(response.body().size,pendapatan,response)
                }
            }

        })
    }

    private fun setData(count:Int,pendapatan:Array<Int>, response: Response<List<Penjualan>>?){
        val yVals = ArrayList<Entry>()
        yVals.clear()
        val valuess:Array<String>
        valuess = Array(count,{i -> response!!.body()[i].tgljual })
        for(i in 0 until count){
            val valu = pendapatan[i]
            yVals.add(Entry(i.toFloat(),valu.toFloat()))
        }

        val set:LineDataSet
        set = LineDataSet(yVals,"Pendapatan")
        val xaxis = chart.xAxis
        xaxis.setValueFormatter(formatter(valuess))
        xaxis.setDrawGridLines(false)
        set.setDrawValues(false)
        set.color  = Color.BLUE
        set.setDrawCircles(false)
        set.lineWidth = 3f
        chart.setNoDataText("Tidak Ada Data")
        chart.setNoDataTextColor(Color.BLUE)


        set.setDrawVerticalHighlightIndicator(false)



        val data = LineData(set)
        chart.data = data
        chart.invalidate()
    }

    public class formatter(private val mValues: Array<String>) :IAxisValueFormatter{
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            return mValues[value.toInt()]
        }
    }

    fun randomString():String {
        val alphabet = "abcdefghijklmnopqrstuvwxyz"
        var s = ""
        val random = Random()
        val randomLen = 1 + random.nextInt(9)
        for (i in 0 until randomLen) {
            val c = alphabet[random.nextInt(26)]
            s += c
        }

        return s
    }



    private fun search(){
//        eCari.addTextChangedListener(object : TextWatcher{
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
//                tJumlahData.text = "Jumlah Data : " + adapter.itemCount
//            }
//        })
    }


    private fun loadData() {
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
                            deletePenjualan(penjualan.idjual)
                        })
//                        recUtang.adapter = adapter
//                        val layoutmanager = LinearLayoutManager(this@LaporanPendapatanActivity)
//                        recUtang.layoutManager = layoutmanager
//                        recUtang.setHasFixedSize(true)
//                        adapter.notifyDataSetChanged()
                        val jumlahData = "Jumlah Data : " + adapter.itemCount
                        tJumlahData.text = jumlahData
                        loadPendapatan()
                        loadChart()
                    }
                }
            }

            override fun onFailure(call: Call<List<Penjualan>>?, t: Throwable?) {
                val alert = AlertDialog.Builder(this@LaporanPendapatanActivity)
                alert.setTitle("ERROR")
                alert.setMessage("terjadi error, coba lagi : "+ t?.message + "\n" + t?.cause)
                alert.setPositiveButton("RETRY",{dialog, which ->
                    loadData()
                    dialog.dismiss()
                })
                alert.show()
            }
        })
    }

    fun loadPendapatan(){
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

    fun setTglAwal(){
        tTglAwal.setOnClickListener {
            DatePickerDialog(this@LaporanPendapatanActivity,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
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
            DatePickerDialog(this@LaporanPendapatanActivity,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
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
        val sdf = SimpleDateFormat("yyyy-MM-dd",Locale.US)

        tTglAwal.text = sdf.format(Date())
        tTglAkhir.text = sdf.format(Date())

    }

    private fun deletePenjualan(idjual: String) {

    }
}
