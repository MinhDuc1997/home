@file:Suppress("DEPRECATION")

package com.example.duc25.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.duc25.fragment.Loading
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.time.Instant
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

open class MainActivity: AppCompatActivity() {
    private var width = 0f
    private var height = 0f
    private var closeApp = 2
    private var loginObj: Login? = null
    private var username: EditText? = null
    private var password: EditText? = null
    private var btnlogin: Button? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkLogined()
        getSizeScreen()
        drawHeader()
        drawLable1()
        drawLable2()
        drawUsername()
        drawPassword()
        drawBtn()
        drawAppName()
        drawDevName()
        username!!.setText("minhducc")
        password!!.setText("01689470862")
        loginObj = Login(this, supportFragmentManager)
        CheckNet().execute("")
    }

    fun checkLogined(){
        try {
            val obj = Db("", this)
            val result = obj.read()
            val json = JSONObject(result)
            val status = json.getString("status_login")
            if (status == "true") {
                val intent = Intent(this, Main3Activity::class.java)
                intent.putExtra("json", json.toString())
                startActivity(intent)
                finish()
            }
        }catch (e: Exception){

        }
    }

    fun event(){
        btnlogin!!.setOnClickListener {
            loginObj!!.checkLogin(username!!.text.toString(), password!!.text.toString())
        }
    }

    private fun getSizeScreen(){
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = (size.x)/100f
        height = (size.y)/100f
    }

    override fun onBackPressed() {
        closeApp--
        if(closeApp > 0){
            Toast.makeText(this, "Nhấn $closeApp lần nữa để thoát", Toast.LENGTH_SHORT).show()
        }
        if(closeApp <=0 ){
            finish()
        }
        return
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    @SuppressLint("StaticFieldLeak")
    inner class CheckNet : AsyncTask<String, String, String>() {
        private var loop = 0
        override fun doInBackground(vararg params: String?): String {
            val timer = Timer()
            timer.scheduleAtFixedRate(0, 500) {
                val net = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo = net.activeNetworkInfo
                if (netInfo != null && netInfo.isConnected) {
                    if(loop == 0) {
                        loop++
                        event()
                        publishProgress("isNet")
                    }
                } else {
                    loop = 0

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

    @SuppressLint("SetTextI18n")
    private fun drawHeader(){
        val header = TextView(this)
        header.text = "Vào nhà"
        header.textSize = width*3
        header.typeface = Typeface.DEFAULT_BOLD
        header.setTextColor(resources.getColor(R.color.ColorSecondary))
        header.measure(0, 0)
        header.y = height*18
        header.x = width*50 - ((header.measuredWidth)/2)
        rl.addView(header)
    }

    @SuppressLint("SetTextI18n")
    private fun drawLable1(){
        val lable1 = TextView(this)
        lable1.text = "Tên người dùng"
        lable1.textSize = width*1.5f
        lable1.typeface = Typeface.DEFAULT_BOLD
        lable1.setTextColor(resources.getColor(R.color.ColorSecondary))
        lable1.measure(0, 0)
        lable1.y = height*30
        lable1.x = width*50 - ((lable1.measuredWidth)/2)
        rl.addView(lable1)
    }

    @SuppressLint("SetTextI18n")
    private fun drawLable2(){
        val lable2 = TextView(this)
        lable2.text = "Mật khẩu"
        lable2.textSize = width*1.5f
        lable2.typeface = Typeface.DEFAULT_BOLD
        lable2.setTextColor(resources.getColor(R.color.ColorSecondary))
        lable2.measure(0, 0)
        lable2.y = height*45
        lable2.x = width*50 - ((lable2.measuredWidth)/2)
        rl.addView(lable2)
    }

    @SuppressLint("SetTextI18n")
    private fun drawUsername(){
        val userN = EditText(this)
        userN.inputType = InputType.TYPE_CLASS_TEXT
        userN.setText("minhducc")
        userN.maxLines = 1
        userN.setHorizontallyScrolling(true)
        userN.setTextColor(resources.getColor(R.color.ColorSecondary))
        userN.gravity = Gravity.CENTER
        userN.width = (width*50).toInt()
        userN.height = (height*7).toInt()
        userN.y = height*35
        userN.x = width*50 - ((width*50)/2)
        username = userN
        rl.addView(username)
    }

    @SuppressLint("SetTextI18n")
    private fun drawPassword(){
        val userP = EditText(this)
        userP.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        userP.setText("01689470862")
        userP.maxLines = 0
        userP.setHorizontallyScrolling(true)
        userP.setTextColor(resources.getColor(R.color.ColorSecondary))
        userP.gravity = Gravity.CENTER
        userP.width = (width*50).toInt()
        userP.height = (height*7).toInt()
        userP.y = height*50
        userP.x = width*50 - ((width*50)/2)
        password = userP
        rl.addView(password)
    }

    @SuppressLint("SetTextI18n")
    private  fun drawBtn(){
        val btn = Button(this)
        btn.text = "Bắt đầu"
        btn.textSize = width*1.1f
        btn.setTextColor(Color.WHITE)
        btn.setBackgroundColor(resources.getColor(R.color.ColorSecondary))
        btn.width = (width*30).toInt()
        btn.height = (height*3).toInt()
        btn.y = height*60
        btn.x = width*50 - ((width*30)/2)
        btnlogin = btn
        rl.addView(btnlogin)
    }

    @SuppressLint("SetTextI18n")
    private fun drawAppName(){
        val appname = TextView(this)
        appname.text = "Home"
        appname.textSize = width*1.3f
        appname.typeface = Typeface.DEFAULT_BOLD
        appname.setTextColor(resources.getColor(R.color.ColorSecondary))
        appname.measure(0, 0)
        appname.y = height*87
        appname.x = width*50 - ((appname.measuredWidth)/2)
        rl.addView(appname)
    }

    @SuppressLint("SetTextI18n")
    private fun drawDevName(){
        val devname = TextView(this)
        devname.text = "@IndieTeam"
        devname.textSize = width*1f
        devname.typeface = Typeface.DEFAULT_BOLD
        devname.setTextColor(resources.getColor(R.color.ColorSecondary))
        devname.measure(0, 0)
        devname.y = height*90
        devname.x = width*50 - ((devname.measuredWidth)/2)
        rl.addView(devname)
    }
}

