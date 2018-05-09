@file:Suppress("DEPRECATION")

package com.example.duc25.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@SuppressLint("Registered")
open class Login : AppCompatActivity() {
    var i = 0


    fun check_login(){
        button.setOnClickListener {
            i++
            if(i==1) {
                Toast.makeText(this@Login, "Đang đăng nhập...", Toast.LENGTH_SHORT).show()
                val uri_api_login: String = "https://techitvn.com/home/api/login.php?username=" + username.getText() + "&password=" + password.getText()
                ReadContentURL().execute(uri_api_login)
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class ReadContentURL : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String {
            val content = StringBuilder()
            val url = URL(params[0])
            val urlConnection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
            val inputStream: InputStream = urlConnection.inputStream
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
            return content.toString()
        }

        fun toActivity(data: String?){
            val intent = Intent(this@Login,Main3Activity::class.java)
            intent.putExtra("json", data.toString())
            startActivity(intent)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val obj = JSONObject(result)
            val status: String = obj.getString("status_login")
            if(status == "true") {
                Toast.makeText(this@Login,"Đã đăng nhập", Toast.LENGTH_SHORT).show()
                toActivity(result)
                finish()
            }
            else{
                Toast.makeText(this@Login,"Sai tên tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                i = 0
            }
        }
    }
}
