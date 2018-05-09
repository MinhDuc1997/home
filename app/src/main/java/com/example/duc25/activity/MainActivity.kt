@file:Suppress("DEPRECATION")

package com.example.duc25.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class MainActivity: Login() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        username.setText("minhducc")
        password.setText("01689470862")

        ReadContentURL().execute("")
    }

    @SuppressLint("StaticFieldLeak")
    inner class ReadContentURL : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg params: String?): String {
            val timer = Timer()
            timer.scheduleAtFixedRate(0, 500) {
                val net = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo = net.activeNetworkInfo
                if (netInfo != null && netInfo.isConnected) {
                    check_login()
                    publishProgress("isNet")
                } else {
                    publishProgress("")
                }
            }
            return ""
        }

        @SuppressLint("SetTextI18n")
        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            if (values[0] == "isNet") {
                textView7.text = ""
                textView7.setBackgroundColor(Color.parseColor("#ffffff"))
                textView7.setTextColor(Color.parseColor("#ffffff"))
            } else{
                textView7.text = "Không có kết nối mạng"
                textView7.setBackgroundColor(Color.parseColor("#ff0000"))
                textView7.setTextColor(Color.parseColor("#ffffff"))
            }
        }
    }

}

