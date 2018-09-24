package com.ahlikasir.aplikasi.kasironline.Activity.transaksi.penjualan

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import kotlinx.android.synthetic.main.activity_cetak_cari.*
import kotlinx.android.synthetic.main.activity_penjualan_cetak.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

class CetakCariActivity : AppCompatActivity() {

    private var mBluetoothAdapter:BluetoothAdapter? = null
    private lateinit var pairedDevice:Set<BluetoothDevice>
    private val ACTION_REQUEST_ENABLE = 1
    lateinit var list: ListView
    lateinit var adapter: ArrayAdapter<*>

    companion object{
        val EXTRA_ADDRESS = "device address"
        val EXTRA_NAME = "device name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cetak_cari)


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(mBluetoothAdapter == null){
            Function().toast("Perangkat tidak support bluetooth",this)
            return
        }

        if(!mBluetoothAdapter!!.isEnabled){
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(intent,ACTION_REQUEST_ENABLE)
        }

        paireddeviceList()

    }

    private fun paireddeviceList(){
        pairedDevice = mBluetoothAdapter!!.bondedDevices
        val list: ArrayList<String> = ArrayList()
        val listAddress:ArrayList<BluetoothDevice> = ArrayList()

        if(!pairedDevice.isEmpty()){
            for(device:BluetoothDevice in pairedDevice){
                list.add(device.name)
                listAddress.add(device)
            }
        }else{
            Function().toast("Tidak Ada Perangkat",this)
        }

        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,list)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
        listView.setOnItemClickListener { parent, view, position, id ->
            val device:BluetoothDevice = listAddress[position]
            val devicename = list[position]
            val address = device.address

            Function().setSharedPrefrences(EXTRA_ADDRESS,address,this)
            Function().setSharedPrefrences(EXTRA_NAME,devicename,this)
            finish()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ACTION_REQUEST_ENABLE){
            if(resultCode == Activity.RESULT_OK){
                if(mBluetoothAdapter!!.isEnabled){
                    Function().toast("Bluetooth Menyala",this)
                }else{
                    Function().toast("Bluetooth Mati",this)
                }
            }else if (resultCode == Activity.RESULT_CANCELED){
                Function().toast("Bluetooth Tercancel",this)
            }
        }
    }

}
