package com.example.duc25.modules

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.example.duc25.activity.Db
import com.example.duc25.config.UriApi
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class HomeService : Service() {
    var dataJson = ""
    private lateinit var token: String
    private lateinit var uriApiMyhome: String

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent != null) {
            token = intent.getStringExtra("content")
            uriApiMyhome = UriApi(null, null, null, null).uriApiMyhome + token
            ReadContentURI().execute(uriApiMyhome)
        }else{
            uriApiMyhome = UriApi(null, null, null, null).uriApiMyhome + token()
            ReadContentURI().execute(uriApiMyhome)
        }

        return START_STICKY
    }

    fun token(): String{
        token = ""
        try {
            val db = Db("", this)
            val result = db.read()
            val json = JSONObject(result)
            val user_info = json.getString("user_info")
            token = JSONObject(user_info).getString("token")
        }catch (e: Exception){

        }
        return token
    }

    @SuppressLint("StaticFieldLeak")
    inner class ReadContentURI : AsyncTask<String, String, String>() {
        lateinit var content:StringBuilder

        fun getHttp(P0: String){
            content = StringBuilder()
            val url = URL(P0)
            val urlConnection = url.openConnection() as HttpsURLConnection
            urlConnection.useCaches = false
            val inputStream = urlConnection.inputStream
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            var line: String?
            try {
                do {
                    line = bufferedReader.readLine()
                    if (line != null) {
                        content.append(line)
                    }
                } while (line != null)
                bufferedReader.close()
            } catch (e: Exception) {
                Log.d("ERROR", e.message)
            }
        }

        override fun doInBackground(vararg params: String): String {
            val net = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = net.activeNetworkInfo
            if (netInfo != null && netInfo.isConnected) {
                getHttp(params[0])
                publishProgress(content.toString())
            }else{
                publishProgress("No network")
            }

            return ""
        }

        @SuppressLint("SetTextI18n")
        override fun onProgressUpdate(vararg values: String?) {
            if(values[0] !== "No network") {
                if(dataJson != values[0]) {
                    val obj = JSONObject(values[0])
                    val status: String = obj.getString("status")
                    if (status == "true") {
                        val jsonObj = JSONObject(values[0])
                        val jsonArr = jsonObj.getJSONArray("light_status")
                        var contentNotification = ""
                        for (i in 0 until jsonArr.length()) {
                            val id_light = jsonArr.getJSONObject(i).getString("id_light")
                            val status_light = jsonArr.getJSONObject(i).getString("status")
                            if (status_light == "0")
                                contentNotification += "Đèn $id_light: Tắt \n"
                            else
                                contentNotification += "Đèn $id_light: Bật \n"
                        }
                        val notification = HomeNotification(this@HomeService)
                        notification.createNotificationChannel()
                        notification.notification(contentNotification)

                    }
                }
                dataJson = values[0]!!
            }else{

            }
            Handler().postDelayed({
                ReadContentURI().execute(uriApiMyhome)
            }, 5000)
        }
    }
}