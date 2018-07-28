package com.indieteam.home.modules

import android.app.Service
import android.content.Intent
import android.os.AsyncTask
import android.os.Handler
import android.os.IBinder
import com.indieteam.home.config.UriApi
import com.indieteam.home.modules.db.Db
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class HomeService : Service() {
    private lateinit var token: String
    private lateinit var uriApiMyhome: String

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent != null) {
            token = intent.getStringExtra("content")
            uriApiMyhome = UriApi(null, null, null, null).uriApiMyhome + token
            Okhttp().request(uriApiMyhome)
        }else{
            uriApiMyhome = UriApi(null, null, null, null).uriApiMyhome + token()
            Okhttp().request(uriApiMyhome)
        }

        return START_STICKY
    }

    private fun token(): String{
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

    inner class Okhttp{

        private val client = OkHttpClient()

        fun request(url: String) {
            val rq = Request.Builder()
                    .url(url)
                    .build()

            client.newCall(rq).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {

                }

                override fun onResponse(call: Call?, response: Response?) {
                    val body = JSONObject( response?.body()?.string())
                    if(body.getString("status") == "true"){
                        val jsonArr = body.getJSONArray("light_status")
                        var contentNotification = ""
                        for (i in 0 until jsonArr.length()) {
                            val id_light = jsonArr.getJSONObject(i).getString("id_light")
                            val status_light = jsonArr.getJSONObject(i).getString("status")
                            contentNotification += if (status_light == "0")
                                "Đèn $id_light: Tắt \n"
                            else
                                "Đèn $id_light: Bật \n"
                        }
                        val notification = HomeNotification(this@HomeService)
                        notification.createNotificationChannel()
                        notification.notification(contentNotification)
                        Loop().execute(url)
                    }
                }
            })
        }
    }

    inner class Loop: AsyncTask<String, String, Void>(){
        override fun doInBackground(vararg params: String?): Void? {
            publishProgress(params[0])
            return null
        }

        override fun onProgressUpdate(vararg values: String) {
             Handler().postDelayed({
                 Okhttp().request(values[0])
                 this@Loop.cancel(true)
             }, 3000)
        }
    }
}