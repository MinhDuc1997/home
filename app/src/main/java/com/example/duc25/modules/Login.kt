@file:Suppress("DEPRECATION")

package com.example.duc25.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.example.duc25.activity.Main3Activity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@SuppressLint("Registered")
class Login(val context: Context){
    var i = 0

    fun checkLogin(username: String, password: String){
        if(i == 0) {
            Toast.makeText(context, "Đang đăng nhập...", Toast.LENGTH_SHORT).show()
            val uriApiLogin = "https://techitvn.com/home/api/login.php?username=$username&password=$password"
            ReadContentURL().execute(uriApiLogin)
        }
        i++
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

        private fun toActivity(data: String?){
            val intent = Intent(context, Main3Activity::class.java)
            intent.putExtra("json", data.toString())
            context.startActivity(intent)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val obj = JSONObject(result)
            val status: String = obj.getString("status_login")
            if(status == "true") {
                Toast.makeText(context,"Đã đăng nhập", Toast.LENGTH_SHORT).show()
                toActivity(result)
            }
            else{
                Toast.makeText(context,"Sai tên tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                i = 0
            }
        }
    }
}
