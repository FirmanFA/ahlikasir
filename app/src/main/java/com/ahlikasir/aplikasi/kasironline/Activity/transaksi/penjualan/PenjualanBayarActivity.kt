package com.ahlikasir.aplikasi.kasironline.Activity.transaksi.penjualan

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.model.transaksi.Pembayaran
import kotlinx.android.synthetic.main.activity_penjualan_bayar.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class PenjualanBayarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penjualan_bayar)

        this.title = "Pembayaran"

        eventHandler()

    }

    fun eventHandler(){
        val bayar = findViewById<EditText>(R.id.jumlahbayar)
        val bayarClick = findViewById<ConstraintLayout>(R.id.bayar)

        bayarClick.setOnClickListener {
            bayarClick()
        }

        jumlahbayar.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(bayar.text.toString() != ""){

                    val totalBayar = Function().getShared("total","",this@PenjualanBayarActivity).toDouble()
                    val df = DecimalFormat("#.##")
                    val totalBayarRounded = df.format(totalBayar)
                    if(bayar.text.toString() != ""){

                            val kembalian = bayar.text.toString().toInt() - totalBayarRounded.toString().toInt()
                            val kembaliRounded = NumberFormat.getNumberInstance(Locale.US).format(kembalian).toString()
                            kembali.setText(kembaliRounded)
                        }

                    }else{
                        kembali.setText("")
                    }

            }
        })

        val faktur = intent.getStringExtra("faktur")
        val totalBayar = Function().getShared("total","",this@PenjualanBayarActivity).toDouble()
        val df = DecimalFormat("#.##")
        val totalBayarRounded = df.format(totalBayar)
        val totalBayarFormatted = NumberFormat.getNumberInstance(Locale.US).format(totalBayarRounded.toInt()).toString()
        etFaktur.setText(faktur)
        totalbayar.setText(totalBayarFormatted)
    }

    fun bayarClick(){
        val bayar = findViewById<EditText>(R.id.jumlahbayar)
        val request = Function().builder()
        val token = Function().token(this)
        val email = Function().email(this)
        val totalBayar = Function().getShared("total","",this@PenjualanBayarActivity).toDouble()
        val df = DecimalFormat("#.##")
        val total = df.format(totalBayar)

        if(bayar.text.toString() == ""){
            Toast.makeText(this,"Isi Jumlah Bayar terlebih Dahulu",Toast.LENGTH_LONG).show()
        }else if(kembali.text.toString().contains("-")){
            Toast.makeText(this,"Jumlah Bayar Harus Melebihi Total",Toast.LENGTH_LONG).show()
        }else{
            val kembalian = bayar.text.toString().toInt() - total.toString().toInt()
            val kembali = kembalian.toString()
            val isiBayar = Pembayaran(etFaktur.text.toString(),total,jumlahbayar.text.toString(),kembali,email)
            request.bayar(isiBayar,token).enqueue(object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    val alert = AlertDialog.Builder(this@PenjualanBayarActivity)
                    alert.setTitle("ERROR")
                    alert.setMessage("terjadi error, coba lagi")
                    alert.setPositiveButton("RETRY",{dialog, which ->
                        bayarClick()
                        dialog.dismiss()
                    })
                    alert.show()
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response!!.isSuccessful){

                        val alert = AlertDialog.Builder(this@PenjualanBayarActivity)
                        alert.setTitle("BERHASIL")
                        alert.setMessage("Ingin Mencetak Struk?")
                        alert.setPositiveButton("CETAK",{ dialog, _ ->

                            val strukKembali = findViewById<EditText>(R.id.kembali)
                            Function().setSharedPrefrences("strukBayar",jumlahbayar.text.toString(),this@PenjualanBayarActivity)
                            Function().setSharedPrefrences("strukTotal",totalbayar.text.toString(),this@PenjualanBayarActivity)
                            Function().setSharedPrefrences("strukKembali",strukKembali.text.toString(),this@PenjualanBayarActivity)
                            Function().setSharedPrefrences(CetakCariActivity.EXTRA_ADDRESS,"Tidak Ada Perangkat",this@PenjualanBayarActivity)
                            Function().setSharedPrefrences(CetakCariActivity.EXTRA_NAME,"Tidak Ada Perangkat",this@PenjualanBayarActivity)
                            val intent = Intent(this@PenjualanBayarActivity,PenjualanCetakActivity::class.java)
                            startActivity(intent)
                            finish()

                            dialog.dismiss()
                        })
                        alert.setNegativeButton("TIDAK",{ dialog, _ ->
                            Function().setSharedPrefrences("fakturLama","",this@PenjualanBayarActivity)
                            Function().setSharedPrefrences("fakturBayar","",this@PenjualanBayarActivity)
                            Function().setSharedPrefrences("pelanggan","kosong",this@PenjualanBayarActivity)
                            Function().setSharedPrefrences("idpelanggan","kosong",this@PenjualanBayarActivity)
                            Function().setSharedPrefrences("barang","kosong",this@PenjualanBayarActivity)
                            Function().setSharedPrefrences("idbarang","kosong",this@PenjualanBayarActivity)
                            finish()
                            dialog.dismiss()
                        })
                        alert.show()


                    }
                }
            })
        }
    }

    override fun finish() {

        super.finish()
    }

}
