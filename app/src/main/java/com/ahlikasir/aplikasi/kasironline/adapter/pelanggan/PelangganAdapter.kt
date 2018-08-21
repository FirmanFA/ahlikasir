package com.ahlikasir.aplikasi.kasironline.adapter.pelanggan

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
import com.ahlikasir.aplikasi.kasironline.model.pelanggan.Pelanggan

/**
 * Created by Firmansyah on 16/05/2018.
 */
class PelangganAdapter(val context: Context, val pelanggan: List<Pelanggan>, val delClick:(Pelanggan)->Unit, val updateClick:(Pelanggan)->Unit): RecyclerView.Adapter<PelangganAdapter.Holder>(), Filterable {

    var filteredpelanggan:List<Pelanggan>

    init {
        this.filteredpelanggan = pelanggan
    }

    override fun getItemCount(): Int {
        return filteredpelanggan.size
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bindPelanggan(filteredpelanggan[position],context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.pelanggan_list_layout,parent,false)
        return Holder(view,delClick,updateClick)
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()){
                    filteredpelanggan = pelanggan
                }else{
                    val filteredlist = ArrayList<Pelanggan>()
                    for(row in pelanggan){
                        if(row.pelanggan.toLowerCase().contains(charString.toLowerCase())){
                            filteredlist.add(row)
                        }
                    }
                    filteredpelanggan = filteredlist
                }
                val filterresult = FilterResults()
                filterresult.values = filteredpelanggan
                return filterresult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredpelanggan = results?.values as ArrayList<Pelanggan>
                notifyDataSetChanged()
            }
        }
    }

    class Holder(itemView: View?, val delClick: (Pelanggan) -> Unit, val updateClick: (Pelanggan) -> Unit):RecyclerView.ViewHolder(itemView){

        val nama = itemView?.findViewById<TextView>(R.id.nama)
        val alamat = itemView?.findViewById<TextView>(R.id.alamat)
        val notelp = itemView?.findViewById<TextView>(R.id.telp)
        val del = itemView?.findViewById<ConstraintLayout>(R.id.wHapusPelanggan)
        val update = itemView?.findViewById<ConstraintLayout>(R.id.wUbahPelanggan)

        fun bindPelanggan(pelanggan: Pelanggan,context: Context){
            nama?.text = pelanggan.pelanggan
            alamat?.text = pelanggan.alamat
            notelp?.text = pelanggan.notelp
            del?.setOnClickListener { delClick(pelanggan) }
            update?.setOnClickListener { updateClick(pelanggan) }

        }
    }

}