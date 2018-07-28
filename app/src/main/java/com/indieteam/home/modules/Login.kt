@file:Suppress("DEPRECATION")

package com.indieteam.home.modules

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.duc25.activity.R
import com.indieteam.home.activity.HomeActivity
import com.indieteam.home.activity.MainActivity
import com.indieteam.home.config.UriApi
import com.indieteam.home.fragment.Loading
import com.indieteam.home.modules.db.Db
import es.dmoral.toasty.Toasty
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


@SuppressLint("Registered")
class Login(val context: MainActivity, private val supportFragmentManager: android.support.v4.app.FragmentManager){
    var i = 0

    fun checkLogin(username: String, password: String){
        if(i == 0) {
            //Toast.makeText(context, "Đang đăng nhập...", Toast.LENGTH_SHORT).show()
            val uriApiLogin = UriApi(username, password, null, null).uriApiLogin
            Okhttp().request(uriApiLogin).toString()
            startFragment()
        }
        i++
    }

    private fun startFragment(){
        supportFragmentManager.beginTransaction().add(R.id.rl, Loading(), "loading")
                .commit()
    }

    private fun removeFragment(){
        supportFragmentManager.beginTransaction().remove(supportFragmentManager.findFragmentByTag("loading"))
                .commit()
    }


    private fun saveData(data: String){
        val obj = Db(data, context)
        obj.insert()
    }

    private fun toActivity(data: String){
        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra("json", data)
        context.startActivity(intent)
        (context as Activity).finish()
    }

    inner class Okhttp() {

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
                    val body = JSONObject(response?.body()?.string())
                    if(body.getString("status_login") == "true"){
                        saveData(body.toString())
                        toActivity(body.toString())
                    }else{
                        removeFragment()
                        context.click = 0
                        context.runOnUiThread {
                            Toasty.error(context, "Sai tên tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT, true).show()
                        }
                    }
                }
            })
        }
    }

}