package com.ahlikasir.aplikasi.kasironline.Activity.laporan

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.Layout
import android.view.View
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.model.barang.Barang
import com.ahlikasir.aplikasi.kasironline.model.pelanggan.Pelanggan
import com.ahlikasir.aplikasi.kasironline.model.toko.Toko
import com.ahlikasir.aplikasi.kasironline.model.toko.TokoType
import com.ahlikasir.aplikasi.kasironline.model.toko.TokoUpIn
import com.ahlikasir.aplikasi.kasironline.model.transaksi.Penjualan
import com.opencsv.CSVWriter
import jxl.CellView
import jxl.Workbook
import jxl.WorkbookSettings
import jxl.format.Alignment
import jxl.format.UnderlineStyle
import jxl.write.*
import jxl.write.Number
import jxl.write.biff.RowsExceededException
import kotlinx.android.synthetic.main.activity_export_excel.*
import kotlinx.android.synthetic.main.activity_laporan_penjualan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import kotlin.text.Typography.times
import java.io.File
import java.net.URLEncoder
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ExportExcelActivity : AppCompatActivity() {

    private var timesBold: WritableCellFormat? = null
    private var times:WritableCellFormat? = null
    private var timesBoldUnderline:WritableCellFormat?=null
    var row = 0
    lateinit var path:String
    lateinit var nama:String
    val REQUEST_EXTERNAL_STORAGE = 1
    lateinit var PERMISSION_STORAGE:Array<String>
    var jumlahKolom = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export_excel)

        PERMISSION_STORAGE = arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)



        path = Environment.getExternalStorageDirectory().toString() + "/Download/"
        try {
            val file = File(Environment.getExternalStorageDirectory().toString() + "/KasirOnline")
            file.mkdirs()
        }catch (e: Exception){

        }
        ePath.setText("Internal Storage/Download/")

    }

    fun export(v:View){
        try{
            val exportType = intent.getStringExtra("exportType")
            if(exportType == "pelanggan"){
                nama = "Laporan Pelanggan"
                jumlahKolom = 4
            }else if(exportType == "barang"){
                nama = "Laporan Barang"
                jumlahKolom = 8
            }else if(exportType == "penjualan"){
                nama = "Laporan Penjualan"
                jumlahKolom = 9
            }
            exportExc()
        }catch (e:Exception){
            Function().toast("error saat export excel " + e.message,this)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ExportExcelActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_EXTERNAL_STORAGE)
            }
        }
    }


    fun exportExc(){
        val file = File(path+nama+".xls")
        val wbsetting = WorkbookSettings()
        wbsetting.locale = Locale("en","EN")

        val workbook = Workbook.createWorkbook(file,wbsetting)
        workbook.createSheet("Report",0)
        val sheet = workbook.getSheet(0)

        createLabel(sheet)
        setExport(sheet,jumlahKolom)

    }


    fun setExport(sheet: WritableSheet, JumlahKolom: Int) {

        val exportType = intent.getStringExtra("exportType")
        val request = Function().builder()
        val token = Function().getShared("token","",this@ExportExcelActivity)
        val email = Function().email(this@ExportExcelActivity)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        request.getTokoByEmail(encodeEmail,token).enqueue(object: retrofit2.Callback<TokoUpIn>{
            override fun onResponse(call: Call<TokoUpIn>?, response: Response<TokoUpIn>?) {
                if(response!!.isSuccessful){
                    try {
                        //set Header
                        val file = File(path+nama+".xls")
                        val wbsetting = WorkbookSettings()
                        wbsetting.locale = Locale("en","EN")

                        val workbook = Workbook.createWorkbook(file,wbsetting)
                        workbook.createSheet("Report",0)
                        val sheet = workbook.getSheet(0)

                        addLabel(sheet,row++,response.body().toko,JumlahKolom)
                        addLabel(sheet,row++,response.body().alamat,JumlahKolom)
                        addLabel(sheet,row++,response.body().telp,JumlahKolom)

                        excelNextLine(sheet,2)
                        //selesai set Header

                        //set sesuai typeExport yang dipilih
                        if(exportType == "pelanggan"){
                            //export pelanggan
                            val judul = arrayOf("No.","Nama Pelanggan","Alamat","Nomor Telp")
                            setJudul(sheet,judul)
                            request.getPelanggan(encodeEmail,token).enqueue(object: Callback<List<Pelanggan>>{
                                override fun onFailure(call: Call<List<Pelanggan>>?, t: Throwable?) {
                                    Function().toast("Something went wrong",this@ExportExcelActivity)
                                }

                                override fun onResponse(call: Call<List<Pelanggan>>?, response: Response<List<Pelanggan>>?) {
                                    if(response!!.isSuccessful){
                                        var no = 1
                                        for(pelanggan in response.body()){
                                            var col = 0
                                            addLabel(sheet,col++,row,no.toString())
                                            addLabel(sheet,col++,row,pelanggan.pelanggan)
                                            addLabel(sheet,col++,row,pelanggan.alamat)
                                            addLabel(sheet,col++,row,pelanggan.notelp)
                                            row++
                                            no++
                                        }
                                        workbook.write()
                                        workbook.close()
                                        Function().toast("Export Berhasil",this@ExportExcelActivity)
                                    }
                                }
                            })
                        }else if(exportType == "barang"){
                            val judul = arrayOf("No.","Nama Barang","Kelompok","Satuan 1","Satuan 2","Harga Satuan 1","Harga Satuan 2","Stok")
                            setJudul(sheet,judul)
                            request.getBarang(encodeEmail,token).enqueue(object: Callback<List<Barang>>{
                                override fun onFailure(call: Call<List<Barang>>?, t: Throwable?) {

                                }

                                override fun onResponse(call: Call<List<Barang>>?, response: Response<List<Barang>>?) {
                                    if(response!!.isSuccessful){
                                        var no = 1
                                        for(barang in response.body()){
                                            val harga1raw = barang.hargasatuan1
                                            val harga2raw = barang.hargasatuan2
                                            val harga1 = NumberFormat.getNumberInstance(Locale.US).format(harga1raw.toInt()).toString()
                                            val harga2 = NumberFormat.getNumberInstance(Locale.US).format(harga2raw.toInt()).toString()
                                            var col = 0
                                            addLabel(sheet,col++,row,no.toString())
                                            addLabel(sheet,col++,row,barang.barang)
                                            addLabel(sheet,col++,row,barang.kelompok)
                                            addLabel(sheet,col++,row,barang.satuan1)
                                            addLabel(sheet,col++,row,barang.satuan2)
                                            addLabel(sheet,col++,row,harga1)
                                            addLabel(sheet,col++,row,harga2)
                                            addLabel(sheet,col++,row,barang.stok)
                                            row++
                                            no++
                                        }
                                        workbook.write()
                                        workbook.close()
                                        Function().toast("Export Berhasil",this@ExportExcelActivity)
                                    }
                                }
                            })
                        }else if (exportType == "penjualan"){
                            val judul = arrayOf("No.","Faktur","Tanggal","Total","Bayar","Kembali","Pelanggan","Alamat","Notelp")
                            setJudul(sheet,judul)

                            val tglawal = intent.getStringExtra("tglawal")
                            val tglakhir = intent.getStringExtra("tglakhir")

                            request.getPenjualan(encodeEmail,tglawal,tglakhir,token).enqueue(object: Callback<List<Penjualan>>{
                                override fun onFailure(call: Call<List<Penjualan>>?, t: Throwable?) {
                                    Function().toast("Something Went Wrong",this@ExportExcelActivity)
                                }

                                override fun onResponse(call: Call<List<Penjualan>>?, response: Response<List<Penjualan>>?) {
                                    if(response!!.isSuccessful){
                                        var no = 1
                                        for(penjualan in response.body()){
                                            val totalraw = penjualan.total
                                            val bayarraw = penjualan.bayar
                                            val kembaliraw = penjualan.kembali
                                            val total = NumberFormat.getNumberInstance(Locale.US).format(totalraw.toInt()).toString()
                                            val bayar = NumberFormat.getNumberInstance(Locale.US).format(bayarraw.toInt()).toString()
                                            val kembali = NumberFormat.getNumberInstance(Locale.US).format(kembaliraw.toInt()).toString()
                                            var col = 0
                                            addLabel(sheet,col++,row,no.toString())
                                            addLabel(sheet,col++,row,penjualan.faktur)
                                            addLabel(sheet,col++,row,penjualan.tgljual)
                                            addLabel(sheet,col++,row,total)
                                            addLabel(sheet,col++,row,bayar)
                                            addLabel(sheet,col++,row,kembali)
                                            addLabel(sheet,col++,row,penjualan.pelanggan)
                                            addLabel(sheet,col++,row,penjualan.alamat)
                                            addLabel(sheet,col++,row,penjualan.notelp)
                                            row++
                                            no++
                                        }
                                        workbook.write()
                                        workbook.close()
                                        Function().toast("Export Berhasil",this@ExportExcelActivity)
                                    }
                                }
                            })

                        }

                    }catch (e:Exception){
                        Function().toast(e.message.toString(),this@ExportExcelActivity)
                    }
                }
            }

            override fun onFailure(call: Call<TokoUpIn>?, t: Throwable?) {

            }
        })
    }


    //      select tblidentitas (toko)
    fun setHeader(csvWriter: CSVWriter, JumlahKolom: Int) {

        val request = Function().builder()
        val token = Function().getShared("token","",this@ExportExcelActivity)
        val email = Function().email(this@ExportExcelActivity)
        val encodeEmail = URLEncoder.encode(email,"UTF-8")
        request.getTokoByEmail(encodeEmail,token).enqueue(object: retrofit2.Callback<TokoUpIn>{
            override fun onResponse(call: Call<TokoUpIn>?, response: Response<TokoUpIn>?) {
                if(response!!.isSuccessful){
                    try {
                        setCenter(csvWriter,JumlahKolom,response.body().toko)
                        setCenter(csvWriter,JumlahKolom,response.body().alamat)
                        setCenter(csvWriter,JumlahKolom,response.body().telp)
                        Function().csvNextLine(csvWriter,2)
                    }catch (e:Exception){

                    }
                }
            }

            override fun onFailure(call: Call<TokoUpIn>?, t: Throwable?) {

            }
        })

    }

    fun setCenter(csvWriter: CSVWriter, JumlahKolom: Int, value: String) {
        try {
            val baru: Int
            if (JumlahKolom % 2 == 1) {
                baru = JumlahKolom - 1
            } else {
                baru = JumlahKolom
            }
            var i: Int
            val a = arrayOfNulls<String>(baru)
            i = 0
            while (i < baru / 2) {
                a[i] = ""
                i++
            }
            a[i] = value
            csvWriter.writeNext(a)
        } catch (e: Exception) {

        }

    }

    @Throws(WriteException::class, RowsExceededException::class)
    private fun createContent(sheet: WritableSheet) {
        val startSum = row + 1
        // Write a few number
        for (i in 1..9) {
            // First column
            addNumber(sheet, 0, row, i + 10)
            // Second column
            addNumber(sheet, 1, row++, i * i)
        }

        val endSum = row
        // Lets calculate the sum of it
        var buf = StringBuffer()
        buf.append("SUM(A$startSum:A$endSum)")
        var f = Formula(0, row, buf.toString())
        sheet.addCell(f)
        buf = StringBuffer()
        buf.append("SUM(B$startSum:B$endSum)")
        f = Formula(1, row, buf.toString())
        sheet.addCell(f)
    }


    @Throws(WriteException::class)
    private fun addCaption(sheet: WritableSheet, column: Int, row: Int, s: String) {
        val label: Label
        label = Label(column, row,  s, timesBold)
        sheet.addCell(label)
    }

    @Throws(WriteException::class, RowsExceededException::class)
    private fun addNumber(sheet: WritableSheet, column: Int, row: Int,
                          integer: Int?) {
        val number: Number
        number = Number(column, row, integer!!.toDouble(), times)
        sheet.addCell(number)
    }

    @Throws(WriteException::class, RowsExceededException::class)
    private fun addLabel(sheet: WritableSheet, column: Int, row: Int, s: String) {
        val label: Label
        label = Label(column, row, s, times)
        sheet.addCell(label)
    }

    @Throws(WriteException::class, RowsExceededException::class)
    private fun addLabel(sheet: WritableSheet, row: Int, s: String, JumlahKolom: Int) {
        var JumlahKolom = JumlahKolom
        val label: Label
        JumlahKolom--
        var newFormat: WritableCellFormat? = null
        newFormat = WritableCellFormat(timesBold)
        newFormat!!.setAlignment(Alignment.CENTRE)
        label = Label(0, row, s, newFormat)
        sheet.addCell(label)
        sheet.mergeCells(0, row, JumlahKolom, row) // parameters -> col1,row1,col2,row2
    }

    @Throws(WriteException::class)
    private fun createLabel(sheet: WritableSheet) {
        // Lets create a times font
        val times10pt = WritableFont(WritableFont.TIMES, 10)
        // Define the cell format
        times = WritableCellFormat(times10pt)

        // create create a bold font with unterlines
        val times10ptBoldUnderline = WritableFont(
                WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE)
        timesBoldUnderline = WritableCellFormat(times10ptBoldUnderline)

        val times10ptBold = WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD, false)
        timesBold = WritableCellFormat(times10ptBold)

        val cv = CellView()
        cv.setFormat(timesBold)
        cv.setAutosize(true)
    }

    fun excelNextLine(sheet: WritableSheet, total: Int): Boolean? {
        try {
            for (i in 0 until total - 1) {
                addLabel(sheet, 0, row++, "")
            }
            return true
        } catch (e: Exception) {
            return false
        }

    }

    @Throws(WriteException::class)
    fun setJudul(sheet: WritableSheet, value : Array<String>) {
        var col = 0
        for (i in value.indices) {
            addCaption(sheet, col++, row, value[i])
        }
        row++
    }

}
