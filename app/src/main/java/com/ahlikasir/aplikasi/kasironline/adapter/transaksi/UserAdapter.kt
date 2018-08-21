package com.ahlikasir.aplikasi.kasironline.adapter.transaksi

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
import com.ahlikasir.aplikasi.kasironline.model.transaksi.User
import java.util.ArrayList

/**
 * Created by Firmansyah on 11/06/2018.
 */
class UserAdapter(val context: Context, val user: List<User>, val delClick:(User)->Unit): RecyclerView.Adapter<UserAdapter.Holder>(),Filterable {

    var filtereduser:List<User>

    init {
        this.filtereduser = user
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()){
                    filtereduser = user
                }else{
                    val filteredlist = ArrayList<User>()
                    for(row in user){
                        if(row.emailuser.toLowerCase().contains(charString.toLowerCase())){
                            filteredlist.add(row)
                        }
                    }
                    filtereduser = filteredlist
                }
                val filterresult = FilterResults()
                filterresult.values = filtereduser
                return filterresult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filtereduser = results?.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bindUser(user[position],context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_list_layout,parent,false)
        return Holder(view,delClick)
    }

    override fun getItemCount(): Int {
        return filtereduser.size
    }


    class Holder(itemView: View?, val delClick: (User) -> Unit):RecyclerView.ViewHolder(itemView) {

        val hapus = itemView?.findViewById<ConstraintLayout>(R.id.wHapusUser)
        val email = itemView?.findViewById<TextView>(R.id.emailUser)
        val telp = itemView?.findViewById<TextView>(R.id.telpUser)

        fun bindUser(user: User,context: Context){
            hapus?.setOnClickListener{ delClick(user) }
            email?.text = user.emailuser
            telp?.text = user.notelp
        }
    }

}