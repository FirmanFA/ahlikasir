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
    lateinit var mmSocket: BluetoothSocket
    lateinit var mmDevice: BluetoothDevice
    private val ACTION_REQUEST_ENABLE = 1


    lateinit var mmOutputStream: OutputStream
    lateinit var mmInputStream: InputStream
    lateinit var workerThread: Thread

    lateinit var readBuffer: ByteArray
    var readBufferPosition: Int = 0
    var counter: Int = 0
    @Volatile var stopWorker: Boolean = false

    var arrayList = ArrayList<String>()
    lateinit var list: ListView
    lateinit var adapter: ArrayAdapter<*>
    var deviceName = ""

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

//        try {
//            findBT()
//            openBT()
//        } catch (e: Exception) {
//            Toast.makeText(this, "Bluetooth Error", Toast.LENGTH_SHORT).show()
//        }

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

//    fun findBT(){
//        try{
//            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//
//            if (mBluetoothAdapter == null) {
//                Toast.makeText(this, "Tidak ada Bluetooth Adapter", Toast.LENGTH_SHORT).show()
//            }
//
//            if(mBluetoothAdapter.isEnabled){
//                val intentBt = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                startActivityForResult(intentBt,0)
//            }
//
//            var paireddevice = mBluetoothAdapter.bondedDevices
//
//            if(paireddevice.size > 0){
//                for (device in paireddevice) {
//                    arrayList.add(device.name)
//                    if (device.name == deviceName) {
////                        mmDevice = device
//                        break
//                    }
//                }
//                adapter.notifyDataSetChanged()
//            }
//
//        }catch (e:NullPointerException){
//            e.printStackTrace()
//        }catch (e:Exception){
//            e.printStackTrace()
//        }
//    }




//    @Throws(IOException::class)
//    fun openBT(){
//        try{
//            val uuidString = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
//            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuidString)
//            mmSocket.connect()
//            mmOutputStream = mmSocket.outputStream
//            mmInputStream = mmSocket.inputStream
//
//            beginListenData()
//
//            Function().toast("Bluetooth berhasil aktif",this)
//
//        }catch (e:Exception){
//
//        }
//    }
//
//    fun beginListenData() {
//        val handler = Handler()
//
//        val delimiter = 10;
//        stopWorker = false
//        readBufferPosition = 0
//        readBuffer = ByteArray(1024)
//
//        workerThread = Thread(Runnable {
//            while (!Thread.currentThread().isInterrupted && !stopWorker){
//
//                try {
//                    val bytesAvailable = mmInputStream.available()
//                    if(bytesAvailable > 0){
//                        val packetBytes = ByteArray(bytesAvailable)
//                        mmInputStream.read(packetBytes)
//                        for (i in 0 until bytesAvailable){
//                            val b = packetBytes[i]
//                            if(b.equals(delimiter)){
//                                val encodeBytes = ByteArray(readBufferPosition)
//                                System.arraycopy(readBuffer,0,encodeBytes,0,encodeBytes.size)
//                                val data = String(encodeBytes, charset("US-ASCII"))
//                                readBufferPosition = 0;
//
//                                handler.post({
//                                    Function().toast(data,this)
//                                })
//                            }else{
//                                readBuffer[readBufferPosition++] = b
//                            }
//                        }
//                    }
//                } catch (e: IOException) {
//                    stopWorker = true;
//                }
//
//            }
//        })
//        workerThread.start()
//    }
}
