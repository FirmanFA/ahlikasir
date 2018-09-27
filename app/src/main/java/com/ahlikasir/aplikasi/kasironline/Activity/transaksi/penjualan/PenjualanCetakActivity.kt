package com.ahlikasir.aplikasi.kasironline.Activity.transaksi.penjualan

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.R
import com.ahlikasir.aplikasi.kasironline.Retrofit.Function
import com.ahlikasir.aplikasi.kasironline.model.toko.TokoUpIn
import com.ahlikasir.aplikasi.kasironline.model.transaksi.Penjualan
import kotlinx.android.synthetic.main.activity_penjualan_cetak.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URLEncoder
import java.text.NumberFormat
import java.util.*

class PenjualanCetakActivity : AppCompatActivity() {

    lateinit var hasil:String
    lateinit var device:String
    lateinit var mBluetoothAdapter: BluetoothAdapter
    lateinit var mmSocket: BluetoothSocket
    lateinit var mmDevice: BluetoothDevice

    lateinit var mmOutputStream: OutputStream
    lateinit var mmInputStream: InputStream
    lateinit var workerThread: Thread
    var flagready = 0

    lateinit var readBuffer: ByteArray
    var readBufferPosition: Int = 0
    var ACTION_REQUEST_ENABLE=1
    @Volatile var stopWorker: Boolean = false

    companion object {
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String
        lateinit var devicename:String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penjualan_cetak)

        device = Function().getShared("printer","",this)

        m_address = Function().getShared(CetakCariActivity.EXTRA_ADDRESS,"",this)
        devicename = Function().getShared(CetakCariActivity.EXTRA_NAME,"",this)
        ePrinter.setText(devicename)

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(mBluetoothAdapter == null){
            Function().toast("Perangkat tidak support bluetooth",this)
            return
        }

        if(!mBluetoothAdapter!!.isEnabled){
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(intent, ACTION_REQUEST_ENABLE)
        }

    }

    class ConnectToDevice(c: Context): AsyncTask<Void, Void, String>() {

        private var connectSucces = true
        private val conext:Context
         init {
            this.conext = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            Function().toast("Loading, Harap Tunggu",conext)
        }

        override fun doInBackground(vararg params: Void?): String? {
            try {
                if (m_bluetoothSocket == null || !m_isConnected) {
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(m_address)
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    m_bluetoothSocket!!.connect()

                }
            } catch (e: IOException) {
                connectSucces = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(connectSucces){
               m_isConnected = true
               Function().toast("Berhasil Connect",conext)
            }else {
                Function().toast("Gagal Connect", conext)
            }
        }

    }


    override fun onResume() {
        device = Function().getShared("printer","",this)
        m_address = Function().getShared(CetakCariActivity.EXTRA_ADDRESS,"",this)
        devicename = Function().getShared(CetakCariActivity.EXTRA_NAME,"",this)
        ePrinter.setText(devicename)
        if(ePrinter.text.toString() == "Tidak Ada Perangkat"){
            Log.i("data","data")
        }else{
            ConnectToDevice(this).execute()
        }

        super.onResume()
    }

    fun preview(v:View){
        setPriview()
        wTeks.visibility = View.VISIBLE
    }

    fun setPriview(){

        val request = Function().builder()
        val token = Function().token(this)
        val emailraw = Function().email(this)
        val email = URLEncoder.encode(emailraw,"UTF-8")
        val faktur = Function().getShared("strukFaktur","",this)
        val tanggalbeli = "Tanggal : " + Function().getShared("strukTanggal","",this)
        val pelanggan = "Pelanggan : " + Function().getShared("strukPelanggan","",this)

        request.getTokoByEmail(email,token).enqueue(object: Callback<TokoUpIn>{
            override fun onResponse(call: Call<TokoUpIn>?, response: Response<TokoUpIn>?) {

                val toko = response!!.body().toko
                val notelp = response.body().telp
                val alamat = response.body().alamat
                tHeader.text = toko + "\n" + alamat + "\n" + notelp
                tFaktur.text = "Faktur : "+faktur
                tPelanggan.text = pelanggan
                tTanggal.text = tanggalbeli

                val header = Function().setCenter(toko)+"\n"+
                             Function().setCenter(alamat)+"\n"+
                             Function().setCenter(notelp)+"\n"+
                             "\n"+
                             faktur+"\n"+
                             tanggalbeli+"\n"+
                             pelanggan+"\n"+
                             Function().getStrip()



                request.getPejualanByFaktur(email,faktur,token).enqueue(object: Callback<List<Penjualan>>{
                    override fun onFailure(call: Call<List<Penjualan>>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<List<Penjualan>>?, response: Response<List<Penjualan>>?) {
                        if(response!!.isSuccessful){
                            var body = ""
                            var view = ""

                            for(i in 0 until response!!.body().size){
                                val barang = response.body()[i].barang
                                val jumlah = response.body()[i].jumlah
                                val hargaRaw = (response.body()[i].hargajual.toInt() / response.body()[i].jumlah.toInt()).toString()
                                val harga = NumberFormat.getNumberInstance(Locale.US).format(hargaRaw.toInt()).toString()
                                val totalRaw = response.body()[i].hargajual
                                val total = NumberFormat.getNumberInstance(Locale.US).format(totalRaw.toInt()).toString()

                                body += barang+"\n"+
                                        jumlah+" x " + harga + "\n"+
                                        Function().setRight(total)+"\n"

                                view += barang+"\n"+
                                        jumlah+" x " + harga + "\n"+
                                        setRight(total)+"\n"

                            }

                            tBarang.text = view
                            body+=Function().getStrip()

                            val bayarRaw = Function().getShared("strukBayar","",this@PenjualanCetakActivity).toInt()
                            val bayar = NumberFormat.getNumberInstance(Locale.US).format(bayarRaw).toString()

                            val totalBayar = "Bayar : "+ bayar
                            val jumlahBayar = "Total : "+ Function().getShared("strukTotal","",this@PenjualanCetakActivity)
                            val kembali = "Kembali : "+Function().getShared("strukKembali", "", this@PenjualanCetakActivity)
                            val caption = "Terimakasih"+"\n"+"Sudah Berbelanja"
                            teks.text = jumlahBayar
                            tBayar.text = totalBayar
                            tKembali.text = kembali
                            tCaption.text = caption

                            val footer = Function().setRight(totalBayar) + "\n" +
                                    Function().setRight(jumlahBayar) + "\n" +
                                    Function().setRight(kembali) + "\n\n" +
                                    Function().setCenter(caption)

                            hasil = header+body+footer
                        }

                    }
                })


            }

            override fun onFailure(call: Call<TokoUpIn>?, t: Throwable?) {

            }
        })
    }

    fun cetak(v:View){
        try {
            if (ePrinter.text.toString().equals("Tidak Ada Perangkat")) {
                Toast.makeText(this, "Tidak ada Printer", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    setPriview()
                } catch (e: Exception) {
                    Toast.makeText(this, "Preview Gagal", Toast.LENGTH_SHORT).show()
                }

                sendData(hasil)

//                val i = Intent(this, Acti::class.java)
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                startActivity(i)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Proses Cetak Gagal, Harap periksa Printer atau bluetooth anda" + e.message, Toast.LENGTH_SHORT).show()
        }

    }

    fun cari(v:View){
        val intent = Intent(this,CetakCariActivity::class.java)
        startActivity(intent)
    }

    fun beginListenData() {
        val handler = Handler()

        val delimiter = 10;
        stopWorker = false
        readBufferPosition = 0
        readBuffer = ByteArray(1024)

        workerThread = Thread(Runnable {
            while (!Thread.currentThread().isInterrupted && !stopWorker){

                try {
                    val bytesAvailable = mmInputStream.available()
                    if(bytesAvailable > 0){
                        val packetBytes = ByteArray(bytesAvailable)
                        mmInputStream.read(packetBytes)
                        for (i in 0 until bytesAvailable){
                            val b = packetBytes[i]
                            if(b.equals(delimiter)){
                                val encodeBytes = ByteArray(readBufferPosition)
                                System.arraycopy(readBuffer,0,encodeBytes,0,encodeBytes.size)
                                val data = String(encodeBytes, charset("US-ASCII"))
                                readBufferPosition = 0;

                                handler.post({
                                    Function().toast(data,this)
                                })
                            }else{
                                readBuffer[readBufferPosition++] = b
                            }
                        }
                    }
                } catch (e:IOException) {
                    stopWorker = true;
                }

            }
        })
        workerThread.start()
    }

    @Throws(IOException::class)
    fun sendData(hasilraw:String){
        var hasil = hasilraw
        hasil += "\n\n\n"

        m_bluetoothSocket!!.outputStream.write(hasil.toByteArray())

        Function().toast("Print Berhasil",this)

    }


    fun setRight(item: String): String {
        val leng = item.length
        var hasil = ""
        for (i in 0 until 32 - leng) {
            if (31 - leng == i) {
                hasil += item
            } else {
                hasil += "  "
            }
        }
        return hasil
    }
}
