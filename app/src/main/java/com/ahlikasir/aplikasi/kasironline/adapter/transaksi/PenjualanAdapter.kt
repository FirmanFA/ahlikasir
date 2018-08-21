package com.ahlikasir.aplikasi.kasironline.adapter.transaksi

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.model.transaksi.Penjualan
import java.text.NumberFormat
import java.util.*

/**
 * Created by Firmansyah on 31/05/2018.
 */
class PenjualanAdapter(val context: Context, val penjualan: List<Penjualan>, val delClick:(Penjualan)->Unit):RecyclerView.Adapter<PenjualanAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.penjualan_list_layout,parent,false)
        return Holder(view,delClick)
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bindPenjualan(penjualan[position],context)
    }

    override fun getItemCount(): Int {
        return penjualan.size
    }

    inner class Holder(itemView: View?, val delClick: (Penjualan) -> Unit):RecyclerView.ViewHolder(itemView) {

        val barang = itemView?.findViewById<TextView>(R.id.tbarang)
        val harga = itemView?.findViewById<TextView>(R.id.tjumlah)
        val delete = itemView?.findViewById<ConstraintLayout>(R.id.wSampah)

        fun bindPenjualan(penjualan: Penjualan,context: Context){
            val numberFormat = NumberFormat.getNumberInstance(Locale.US).format(penjualan.hargajual.toInt()).toString()
            barang?.text = "${penjualan.barang} x ${penjualan.jumlah}"
            harga?.text = "Rp. " + numberFormat
            delete?.setOnClickListener { delClick(penjualan) }
        }
    }
}