@file:Suppress("DEPRECATION")

package com.example.duc25.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.example.duc25.fragment.Loading
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import android.app.Activity




@SuppressLint("Registered")
class Login(val context: Context, val supportFragmentManager: android.support.v4.app.FragmentManager){
    var i = 0

    fun checkLogin(username: String, password: String){
        if(i == 0) {
            //Toast.makeText(context, "Đang đăng nhập...", Toast.LENGTH_SHORT).show()
            val uriApiLogin = "https://techitvn.com/home/api/login.php?username=$username&password=$password"
            ReadContentURL().execute(uriApiLogin)
            startFragment()
        }
        i++
    }

    fun startFragment(){
        supportFragmentManager.beginTransaction().add(R.id.rl, Loading(), "loading")
                .commit()
    }

    fun removeFragment(){
        supportFragmentManager.beginTransaction().remove(supportFragmentManager.findFragmentByTag("loading"))
                .commit()
    }

    @SuppressLint("StaticFieldLeak")
    inner class ReadContentURL : AsyncTask<String, String, String>() {
        lateinit var content: StringBuilder
        fun getHttp(P0: String){
            try {
                content = StringBuilder()
                val url = URL(P0)
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
            }catch (e: Exception){}
        }

        private fun toActivity(data: String){
            val intent = Intent(context, Main3Activity::class.java)
            intent.putExtra("json", data)
            context.startActivity(intent)
            (context as Activity).finish()
        }

        private fun saveData(data: String){
            val obj = Db(data, context)
            obj.insert()
        }

        override fun doInBackground(vararg params: String): String {
            getHttp(params[0])
            if(content.toString().isNotBlank())
                publishProgress("")
            else
                publishProgress("Error network")
            return ""
        }

        override fun onProgressUpdate(vararg values: String) {
            super.onProgressUpdate(*values)
            if(values[0] !== "Error network") {
                val obj = JSONObject(content.toString())
                val status: String = obj.getString("status_login")
                removeFragment()
                if (status == "true") {
                    saveData(content.toString())
                    toActivity(content.toString())
                } else {
                    Toast.makeText(context, "Sai tên tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                    i = 0
                }
            }else {
                removeFragment()
                Toast.makeText(context, "Lỗi mạng", Toast.LENGTH_SHORT).show()
                i = 0
            }
        }
    }
}
