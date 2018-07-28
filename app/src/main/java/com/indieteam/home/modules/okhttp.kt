package com.indieteam.home.modules

import android.app.Activity
import android.widget.Toast
import es.dmoral.toasty.Toasty
import okhttp3.*
import java.io.IOException

class Okhttp(val context: Activity) {

    private val client = OkHttpClient()

    fun request(url: String) {
        val rq = Request.Builder()
                .url(url)
                .build()

        client.newCall(rq).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                context.runOnUiThread {
                    Toasty.error(context, "Lỗi mạng", Toast.LENGTH_SHORT, true).show()
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                body?.let {
                    context.runOnUiThread {
                        Toasty.success(context, body, Toast.LENGTH_SHORT, true).show()
                    }
                }
            }
        })
    }
}