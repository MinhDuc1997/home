package com.example.duc25.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.Toast
import com.example.duc25.modules.HomeService
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.android.synthetic.main.app_bar_main3.*
import kotlinx.android.synthetic.main.content_main3.*
import kotlinx.android.synthetic.main.listview.*
import kotlinx.android.synthetic.main.nav_header_main3.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


@Suppress("DEPRECATION")
open class Main3Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var jsonDataUser: JSONObject
    lateinit var update: String
    lateinit var light: JSONObject
    private var closeApp = 2
    private lateinit var packageInfo: PackageInfo
    private lateinit var versionName: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        setSupportActionBar(toolbar)
        list_view.visibility = View.GONE
        light_name.text = ""
        light_on_off.text = ""
        light_status.text = ""
        getDataJson()
        val uriApiMyhome: String = "https://techitvn.com/home/api/myhome.php?token=" + getToken()

        swipeResfresh.setOnRefreshListener {
            startActivity(intent)
            finish()
            swipeResfresh.isRefreshing = false
        }

        ReadContentUri().execute(uriApiMyhome)
    }

    private fun getDataJson(){
        val jsonUsers = intent.getStringExtra("json")
        val jsonObj = JSONObject(jsonUsers)
        val info: String = jsonObj.getString("user_info")
        val jsonObjInfo = JSONObject(info)
        jsonDataUser = jsonObjInfo
    }

    private fun getToken(): String{
        return jsonDataUser.getString("token")
    }

    private fun setInfo(){
        val username: String = jsonDataUser.getString("username")
        val email: String = jsonDataUser.getString("email")
        profile_user_name.text = username
        profile_user_email.text = email
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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        userInfo()
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main3, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_settings -> {
//                startActivity(intent)
//                return true}
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

    @SuppressLint("SetTextI18n")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val jsonArr = light.getJSONArray("light_status")

        when (item.itemId) {
            R.id.nav_light -> {
                list_view.visibility = View.VISIBLE
                val switchBtn = Switch(this)

                var arrayView = listOf<FieldValue>()
                for(i in 0 until jsonArr.length()) {
                    val idLight = jsonArr.getJSONObject(i).getString("id_light")
                    val status = jsonArr.getJSONObject(i).getString("status")
                    var change: String
                    change = if(status == "0"){
                        "Off"
                    }else{
                        "On"
                    }
                    arrayView += FieldValue(idLight, change, switchBtn)

                }
                list_view.adapter = CustomAdapter(this, R.layout.field_listview, arrayView, "đèn", getToken())
                light_lable.visibility = View.GONE
                light_name.text = "Đèn"
                light_status.text = "Trạng thái"
                light_on_off.text = "Công tắc"
                title = "Đèn"
            }
//            R.id.nav_fan -> {
//                list_view.visibility = View.GONE
//                light_lable.text = ""
//                light_name.text = ""
//                light_on_off.text = ""
//                light_status.text = ""
//            }
            R.id.about -> {
                list_view.visibility = View.GONE
                light_lable.visibility = View.VISIBLE
                try {
                    packageInfo = packageManager.getPackageInfo(getPackageName(), 0)
                    versionName = packageInfo.versionName
                    light_lable.text = "App Name: " + resources.getString(R.string.app_name) +
                            "\n Version: $versionName (private beta)" +
                            "\n Developer: Nguyen Minh Duc " +
                            "\n Team: " + resources.getString(R.string.textview8) +
                            "\n\n What news: \n" + resources.getString(R.string.whatnew)
                } catch (e: Exception) {
                }
                light_name.text = ""
                light_on_off.text = ""
                light_status.text = ""
                title = "About"
            }
            R.id.nav_music -> {
                list_view.visibility = View.VISIBLE
                val switchBtn = Switch(this)

                val arrayView = listOf(
                        FieldValue("Tell me you ...","Play", switchBtn),
                        FieldValue("Galaxy","Stop", switchBtn),
                        FieldValue("Mr.Chu","Stop", switchBtn),
                        FieldValue("Back","Stop", switchBtn),
                        FieldValue("Fix me","Stop", switchBtn),
                        FieldValue("Blue","Stop", switchBtn),
                        FieldValue("Pick me","Stop", switchBtn),
                        FieldValue("So so","Stop", switchBtn),
                        FieldValue("First Love","Stop", switchBtn),
                        FieldValue("Not him","Stop", switchBtn)
                )
                list_view.adapter = CustomAdapter(this, R.layout.field_listview, arrayView, "bài", getToken())
                light_lable.visibility = View.GONE
                light_name.text = "Bài hát"
                light_status.text = "Trạng thái"
                light_on_off.text = "Chơi/Dừng"
                title = "Chơi nhạc"
            }
            R.id.for_developer -> {
                if(update == "updated") {
                    list_view.visibility = View.GONE
                    light_lable.text = ""
                    light_name.text = ""
                    light_on_off.text = ""
                    light_status.text = ""
                    var temp = ""
                    for(i in 0 until jsonArr.length()) {
                        val idLight = jsonArr.getJSONObject(i).getString("id_light")
                        val status = jsonArr.getJSONObject(i).getString("status")
                        temp += "id_light: $idLight, status: $status \n"
                    }
                    title = "Raw Json"
                    light_lable.visibility = View.VISIBLE
                    light_lable.text = jsonDataUser.toString() + "\n\n" + light.toString()
                    Toast.makeText(this, temp, Toast.LENGTH_LONG).show()
                }
            }
            R.id.logout -> {
                Confim(this).show(fragmentManager, "logout")
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    @SuppressLint("StaticFieldLeak")
    inner class ReadContentUri : AsyncTask<String, String, String>() {
        lateinit var content:StringBuilder

        private fun getHttp(P0: String){
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

        @SuppressLint("SetTextI18n", "RtlHardcoded")
        override fun onProgressUpdate(vararg values: String?) {
            if(values[0] !== "No network") {
                val obj = JSONObject(values[0])
                val status: String = obj.getString("status")
                light = obj
                if (status == "true") {
                    val i = Intent(this@Main3Activity, HomeService::class.java)
                    i.putExtra("content", getToken())
                    startService(i)
                    update = "updated"
                    light_lable.visibility = View.VISIBLE
                    light_lable.text = "Dữ liệu đã được cập nhật"

                    val toggle = ActionBarDrawerToggle(
                            this@Main3Activity, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
                    drawer_layout.addDrawerListener(toggle)
                    toggle.syncState()
                    nav_view.setNavigationItemSelectedListener(this@Main3Activity)
                    drawer_layout.openDrawer(Gravity.LEFT)
                    setInfo()
                }
            }else{
                light_lable.text = "Không có kết nối mạng"
                light_lable.setTextColor(resources.getColor(R.color.ColorSecondary))
            }
        }
    }

}
