package com.ahlikasir.aplikasi.kasironline.adapter.satuan

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
import com.ahlikasir.aplikasi.kasironline.model.satuan.Satuan

/**
 * Created by Firmansyah on 15/05/2018.
 */
class SatuanAdapter(val context: Context, val satuan: List<Satuan>, val delClick:(Satuan)->Unit,val updateClick:(Satuan)->Unit): RecyclerView.Adapter<SatuanAdapter.Holder>(), Filterable {

    var filteredsatuan:List<Satuan>

    init {
        this.filteredsatuan = satuan
    }

    override fun getItemCount(): Int {
        return filteredsatuan.size
    }

    override fun onBindViewHolder(holder: SatuanAdapter.Holder?, position: Int) {
        holder?.bindSatuan(filteredsatuan[position],context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SatuanAdapter.Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.satuan_list_layout,parent,false)
        return Holder(view,delClick,updateClick)
    }

    class Holder(itemView:View?,val delClick: (Satuan) -> Unit,val updateClick: (Satuan) -> Unit):RecyclerView.ViewHolder(itemView){

        val satuanet = itemView?.findViewById<TextView>(R.id.satuanAll)
        val delete = itemView?.findViewById<ConstraintLayout>(R.id.wHapusSatuan)
        val update = itemView?.findViewById<ConstraintLayout>(R.id.wUbahSatuan)

        fun bindSatuan(satuan: Satuan,context: Context){
            satuanet?.text = satuan.nilai1 + " " + satuan.satuan1 + " isi " + satuan.nilai2 + " " + satuan.satuan2
            delete?.setOnClickListener { delClick(satuan) }
            update?.setOnClickListener { updateClick(satuan) }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()){
                    filteredsatuan = satuan
                }else{
                    val filteredlist = ArrayList<Satuan>()
                    for(row in satuan){
                        if(row.satuan1.toLowerCase().contains(charString.toLowerCase()) || row.satuan2.toLowerCase().contains(charString.toLowerCase())){
                            filteredlist.add(row)
                        }
                    }
                    filteredsatuan = filteredlist
                }
                val filterresult = FilterResults()
                filterresult.values = filteredsatuan
                return filterresult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredsatuan = results?.values as ArrayList<Satuan>
                notifyDataSetChanged()
            }
        }
    }


}