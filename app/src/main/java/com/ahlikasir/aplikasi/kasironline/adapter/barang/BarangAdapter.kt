package com.ahlikasir.aplikasi.kasironline.adapter.barang

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
import com.ahlikasir.aplikasi.kasironline.model.barang.Barang
import java.text.NumberFormat
import java.util.*

/**
 * Created by Firmansyah on 17/05/2018.
 */
class BarangAdapter(val context: Context, val barang: List<Barang>, val delClick:(Barang)->Unit, val updateClick:(Barang)->Unit): RecyclerView.Adapter<BarangAdapter.Holder>(), Filterable {

    var filteredbarang:List<Barang>

    init {
        this.filteredbarang = barang
    }

    override fun getItemCount(): Int {
        return filteredbarang.size
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bindBarang(filteredbarang[position],context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.barang_list_layout,parent,false)
        return Holder(view,delClick,updateClick)
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()){
                    filteredbarang = barang
                }else{
                    val filteredlist = ArrayList<Barang>()
                    for(row in barang){
                        if(row.barang.toLowerCase().contains(charString.toLowerCase())){
                            filteredlist.add(row)
                        }
                    }
                    filteredbarang = filteredlist
                }
                val filterresult = FilterResults()
                filterresult.values = filteredbarang
                return filterresult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredbarang = results?.values as ArrayList<Barang>
                notifyDataSetChanged()
            }
        }
    }

    class Holder(itemView: View?, val delClick: (Barang) -> Unit, val updateClick: (Barang) -> Unit):RecyclerView.ViewHolder(itemView) {

        val namabarang = itemView?.findViewById<TextView>(R.id.barang)
        val harga1 = itemView?.findViewById<TextView>(R.id.harga1)
        val harga2 = itemView?.findViewById<TextView>(R.id.harga2)
        val del = itemView?.findViewById<ConstraintLayout>(R.id.wHapusBarang)
        val update = itemView?.findViewById<ConstraintLayout>(R.id.wUbahBarang)

        fun bindBarang(barang:Barang,context: Context){
            val numberformat1 = NumberFormat.getNumberInstance(Locale.US).format(barang.hargasatuan1.toInt()).toString()
            val numberformat2 = NumberFormat.getNumberInstance(Locale.US).format(barang.hargasatuan2.toInt()).toString()
            namabarang?.text = barang.barang
            harga1?.text = "Rp. $numberformat1/${barang.satuan1}"
            harga2?.text = "Rp. $numberformat2/${barang.satuan2}"
            del?.setOnClickListener { delClick(barang) }
            update?.setOnClickListener { updateClick(barang) }
        }
    }
}