package com.ahlikasir.aplikasi.kasironline.adapter.laporan

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.model.transaksi.Penjualan
import kotlinx.android.synthetic.main.laporan_pendapatan_list_layout.view.*
import java.text.NumberFormat
import java.util.*

/**
 * Created by Firmansyah on 29/06/2018.
 */
class LaporanPendapatanAdapter(val context: Context, val penjualan: List<Penjualan>, val delClick:(Penjualan)->Unit): RecyclerView.Adapter<LaporanPendapatanAdapter.Holder>(),Filterable {

    var filteredpenjualan:List<Penjualan>

    init {
        this.filteredpenjualan = penjualan
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.laporan_pendapatan_list_layout,parent,false)
        return Holder(view,delClick)
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bindPenjualan(filteredpenjualan[position],context)
    }

    override fun getItemCount(): Int {
        return filteredpenjualan.size
    }

    inner class Holder(itemView: View?, val delClick: (Penjualan) -> Unit): RecyclerView.ViewHolder(itemView) {

        val pelanggan = itemView?.findViewById<TextView>(R.id.tbarang)
        val tanggalBeli = itemView?.findViewById<TextView>(R.id.tTgl)
        val faktur = itemView?.findViewById<TextView>(R.id.tFaktur)
        val total = itemView?.findViewById<TextView>(R.id.tTotal)
        val delete = itemView?.findViewById<ConstraintLayout>(R.id.hapusBtn)

        fun bindPenjualan(penjualan: Penjualan, context: Context){
            if(penjualan.total == null){
                total?.text = "Total : Rp. 0"
            }else{
                val numberFormatTotal ="Total : Rp. " + NumberFormat.getNumberInstance(Locale.US).format(penjualan.total.toInt()).toString()
                total?.text = numberFormatTotal
            }
            faktur?.text = penjualan.faktur

            pelanggan?.text = penjualan.pelanggan
            tanggalBeli?.text = penjualan.tgljual

            delete?.setOnClickListener { delClick(penjualan) }
        }
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()){
                    filteredpenjualan = penjualan
                }else{
                    val filteredlist = ArrayList<Penjualan>()
                    for(row in penjualan){
                        if(row.pelanggan.toLowerCase().contains(charString.toLowerCase())){
                            filteredlist.add(row)
                        }
                    }
                    filteredpenjualan = filteredlist
                }
                val filterresult = FilterResults()
                filterresult.values = filteredpenjualan
                return filterresult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredpenjualan = results?.values as ArrayList<Penjualan>
                notifyDataSetChanged()
            }
        }
    }
}