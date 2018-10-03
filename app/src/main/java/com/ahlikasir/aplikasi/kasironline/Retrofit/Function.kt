package com.ahlikasir.aplikasi.kasironline.Retrofit

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import android.widget.Toast
import com.ahlikasir.aplikasi.kasironline.Client.BloonClient
import com.opencsv.CSVWriter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import se.simbio.encryption.Encryption

class Function {

    lateinit var sharedPrefrences:SharedPreferences

    fun builder(): BloonClient {
        val http = "http://45.77.33.56/ukom/kel1/index.php/"
        val okhttpClientBuilder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        okhttpClientBuilder.addInterceptor(logging)
        val builder = Retrofit.Builder().baseUrl(http)
                .addConverterFactory(GsonConverterFactory.create()).client(okhttpClientBuilder.build())

        val retrofit = builder.build()

        val client: BloonClient = retrofit.create(BloonClient::class.java)
        return client
    }

    fun builderEmail(): BloonClient {
        val http = "http://emailapi.com/api/219b77b3/"
        val okhttpClientBuilder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        okhttpClientBuilder.addInterceptor(logging)
        val builder = Retrofit.Builder().baseUrl(http)
                .addConverterFactory(GsonConverterFactory.create()).client(okhttpClientBuilder.build())

        val retrofit = builder.build()

        val client: BloonClient = retrofit.create(BloonClient::class.java)
        return client
    }

    fun init(context: Context){
        sharedPrefrences = context.getSharedPreferences("coba",Context.MODE_PRIVATE)
    }

    fun token(context: Context):String{
        val token = getShared("token","",context)
        return if(token != ""){
            token
        }else{
            "error"
        }

    }

    fun email(context: Context):String{
        val token = getShared("email","",context)
        return if(token != ""){
            token
        }else{
            "error"
        }

    }

    fun setSharedPrefrences(name:String,value:String,context: Context){
        init(context)
        val editor = sharedPrefrences.edit()
        editor.putString(name,value)
        editor.apply()
    }

    fun getShared(name:String,default:String,context: Context):String{
        init(context)
        return sharedPrefrences.getString(name,default)
    }

    fun setCenter(item: String): String {
        val leng = item.length
        var hasil = ""
        for (i in 0 until 32 - leng) {
            if ((32 - leng) / 2 + 1 == i) {
                hasil += item
            } else {
                hasil += " "
            }
        }
        return hasil
    }

    fun getStrip(): String {
        var a = ""
        for (i in 0..31) {
            a += "-"
        }
        return a + "\n"
    }

    fun setRight(item: String): String {
        val leng = item.length
        var hasil = ""
        for (i in 0 until 32 - leng) {
            if (31 - leng == i) {
                hasil += item
            } else {
                hasil += " "
            }
        }
        return hasil
    }

    fun getDecrypt(teks: String): String {
        try {
            val key = "KomputerKit"
            val salt = "KomputerKit"
            val iv = ByteArray(16)
            val encryption = Encryption.getDefault(key,salt,iv)

            return encryption.decryptOrNull(teks)
        } catch (e: Exception) {
            return ""
        }

    }

    fun getDeviceID(c: ContentResolver): String {
        return Settings.Secure.getString(c, Settings.Secure.ANDROID_ID)
    }

    fun toast(teks:String,context: Context){
        Toast.makeText(context,teks,Toast.LENGTH_LONG).show()
    }

    fun csvNextLine(csvWriter: CSVWriter): Boolean {
        try {
            val header = arrayOf("", "", "")
            csvWriter.writeNext(header)
            return true
        } catch (e: Exception) {
            return false
        }

    }

    fun csvNextLine(csvWriter: CSVWriter, total: Int): Boolean {
        try {
            val header = arrayOf("", "", "")
            for (i in 0 until total) {
                csvWriter.writeNext(header)
            }
            return true
        } catch (e: Exception) {
            return false
        }

    }

    fun setCenter(csvWriter: CSVWriter, JumlahKolom: Int, value: String): Boolean {
        try {
            val item = arrayOf<String>()
            val baru = JumlahKolom / 2 - 1
            var i: Int
            i = 0
            while (i < baru) {
                item[i] = ""
                i++
            }
            item[i] = value
            csvWriter.writeNext(item)
            return true
        } catch (e: Exception) {
            return false
        }

    }

}