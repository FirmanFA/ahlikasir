package com.ahlikasir.aplikasi.kasironline.adapter.kelompok

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
import com.ahlikasir.aplikasi.kasironline.model.kelompok.Kelompok

/**
 * Created by Firmansyah on 09/05/2018.
 */
class KelompokAdapter(val context: Context,val kelompok: List<Kelompok>,val deleteClick:(Kelompok) -> Unit,val updateClick:(Kelompok) -> Unit):RecyclerView.Adapter<KelompokAdapter.Holder>(),Filterable {

    var jumlahData = 0
    var filteredkelompok:List<Kelompok>
    val ITEM = 0
    val LOADING = 1


    init {
        this.filteredkelompok = kelompok
        this.jumlahData = jumlahData
    }

    override fun getItemCount(): Int {
        return filteredkelompok.size
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bindKelompok(filteredkelompok[position],context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.kelompok_list_layout,parent,false)

        return Holder(view,deleteClick,updateClick)
    }



    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()){
                    filteredkelompok = kelompok
                }else{
                    val filteredlist = ArrayList<Kelompok>()
                    for(row in kelompok){
                        if(row.kelompok.toLowerCase().contains(charString.toLowerCase())){
                            filteredlist.add(row)
                        }
                    }
                    filteredkelompok = filteredlist
                    jumlahData = filteredkelompok.size
                }
                val filterresult = FilterResults()
                filterresult.values = filteredkelompok
                return filterresult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredkelompok = results?.values as ArrayList<Kelompok>
                notifyDataSetChanged()
            }
        }
    }

    class Holder(itemView:View?, val deleteClick: (Kelompok) -> Unit, val updateClick: (Kelompok) -> Unit):RecyclerView.ViewHolder(itemView) {

        val kel = itemView?.findViewById<TextView>(R.id.KelompokTxt)
        val del = itemView?.findViewById<ConstraintLayout>(R.id.kelDelete)
        val update = itemView?.findViewById<ConstraintLayout>(R.id.kelUpdate)

        fun bindKelompok(kelompok: Kelompok,context: Context){
            kel?.text = kelompok.kelompok
            del?.setOnClickListener{deleteClick(kelompok)}
            update?.setOnClickListener { updateClick(kelompok) }
        }

//        private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
//            val viewHolder: RecyclerView.ViewHolder
//            val v1 = inflater.inflate(R.layout.kelompok_list_layout, parent, false)
//            viewHolder = MovieVH(v1)
//            return viewHolder
//        }
//
//        fun whenType(viewType: Int,ITEM: Int,LOADING: Int,parent: ViewGroup?,context: Context):RecyclerView.ViewHolder{
//            var viewHolder:RecyclerView.ViewHolder? = null
//            val inflater = LayoutInflater.from(context)
//
//            when(viewType){
//                ITEM -> viewHolder = getViewHolder(parent!!,inflater)
//                LOADING -> {
//                    val v2 = inflater.inflate(R.layout.item_progress,parent,false)
//                    viewHolder = LoadingVH(v2)
//                }
//
//            }
//            return viewHolder!!
//        }

    }
}