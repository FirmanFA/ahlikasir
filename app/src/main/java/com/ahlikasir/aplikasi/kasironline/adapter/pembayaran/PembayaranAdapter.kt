package com.ahlikasir.aplikasi.kasironline.adapter.pembayaran

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.model.transaksi.Pembayaran
import java.text.NumberFormat
import java.util.*

class PembayaranAdapter(val context: Context,val bayar:List<Pembayaran>,val itemClick:(Pembayaran) -> Unit):RecyclerView.Adapter<PembayaranAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bindBayar(bayar[position],context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.pembayaran_list_layout,parent,false)
        return Holder(view,itemClick)
    }

    override fun getItemCount(): Int {
        return bayar.count()
    }

    class Holder(itemView: View?,val itemClick: (Pembayaran) -> Unit):RecyclerView.ViewHolder(itemView) {

        val faktur = itemView?.findViewById<TextView>(R.id.fakturTxt)
        val nama = itemView?.findViewById<TextView>(R.id.pelangganBeli)
        val harga = itemView?.findViewById<TextView>(R.id.jumlahTxt)
        val btnBayar = itemView?.findViewById<ConstraintLayout>(R.id.wSampah)

        fun bindBayar(bayar:Pembayaran,context: Context){
            val totalFormat = NumberFormat.getNumberInstance(Locale.US).format(bayar.total.toString().toInt()).toString()
            faktur?.text = bayar.faktur
            nama?.text = bayar.pelanggan
            harga?.text = "Rp. " + totalFormat
            btnBayar?.setOnClickListener { itemClick(bayar) }
        }
    }


}