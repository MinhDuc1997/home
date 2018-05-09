package com.example.duc25.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.android.synthetic.main.app_bar_main3.*
import kotlinx.android.synthetic.main.content_main3.*
import kotlinx.android.synthetic.main.nav_header_main3.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


open class Main3Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var jsonDataUser: JSONObject
    lateinit var update: String
    lateinit var light: JSONObject

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        setSupportActionBar(toolbar)
        getDataJson()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val uri_api_myhome: String = "https://techitvn.com/home/api/myhome.php?token=" + token()

        ReadContentURL().execute(uri_api_myhome)
    }

    private fun getDataJson(){
        val json_users = intent.getStringExtra("json")
        val json_obj = JSONObject(json_users)
        val info: String = json_obj.getString("user_info")
        val json_obj_info = JSONObject(info)
        jsonDataUser = json_obj_info

    }

    private fun token(): String{
        return jsonDataUser.getString("token")
    }

    private fun user_info(){
        val username: String = jsonDataUser.getString("username")
        val email: String = jsonDataUser.getString("email")
        profile_user_name.text = username
        profile_user_email.text = email
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        user_info()
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main3, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this@Main3Activity,"Refreshing...", Toast.LENGTH_SHORT).show()
                finish()
                startActivity(getIntent())
                return true}
            else -> return super.onOptionsItemSelected(item)
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val json_arr = light.getJSONArray("light_status")

        when (item.itemId) {
            R.id.nav_light -> {
                list_view.visibility = View.VISIBLE
                val switch_bttn = Switch(this)

                var array_view = listOf<fieldValue>()
                for(i in 0 until json_arr.length()) {
                    val id_light = json_arr.getJSONObject(i).getString("id_light")
                    val status = json_arr.getJSONObject(i).getString("status")
                    var change: String
                    if(status == "0"){
                        change = "Off"
                    }else{
                        change = "On"
                    }
                    array_view += fieldValue(id_light, change, switch_bttn)

                }

                list_view.adapter = CustomAdapter(this, R.layout.field_listview, array_view, "đèn", token())
                light_lable.text = "Quản lý đèn"
                light_name.text = "Đèn"
                light_status.text = "Trạng thái"
                light_on_off.text = "Công tắc"
            }
            R.id.nav_fan -> {
                list_view.visibility = View.VISIBLE
                val switch_bttn = Switch(this)

                val array_view = listOf<fieldValue>(
                        fieldValue("1","Off", switch_bttn),
                        fieldValue("2","On", switch_bttn),
                        fieldValue("3","On", switch_bttn)
                )
                list_view.adapter = CustomAdapter(this, R.layout.field_listview, array_view, "quạt", token())
                light_lable.text = "Quản lý quạt"
                light_name.text = "Quạt"
                light_status.text = "Trạng thái"
                light_on_off.text = "Công tắc"
            }
            R.id.nav_camera -> {
                list_view.visibility = View.VISIBLE
                val switch_bttn = Switch(this)

                val array_view = listOf<fieldValue>(
                        fieldValue("1","Off", switch_bttn),
                        fieldValue("2","On", switch_bttn)
                )
                list_view.adapter = CustomAdapter(this, R.layout.field_listview, array_view, "camera", token())
                light_lable.text = "Quản lý camera trong nhà"
                light_name.text = "Camera"
                light_status.text = "Trạng thái"
                light_on_off.text = "Công tắc"
            }
            R.id.nav_music -> {
                list_view.visibility = View.VISIBLE
                val switch_bttn = Switch(this)

                val array_view = listOf<fieldValue>(
                        fieldValue("Tell me you ...","Play", switch_bttn),
                        fieldValue("Galaxy","Stop", switch_bttn),
                        fieldValue("Mr.Chu","Stop", switch_bttn),
                        fieldValue("Back","Stop", switch_bttn),
                        fieldValue("Fix me","Stop", switch_bttn),
                        fieldValue("Blue","Stop", switch_bttn),
                        fieldValue("Pick me","Stop", switch_bttn),
                        fieldValue("So so","Stop", switch_bttn),
                        fieldValue("First Love","Stop", switch_bttn),
                        fieldValue("Not him","Stop", switch_bttn)
                        )
                list_view.adapter = CustomAdapter(this, R.layout.field_listview, array_view, "bài", token())
                light_lable.text = "Phát nhạc"
                light_name.text = "Bài hát"
                light_status.text = "Trạng thái"
                light_on_off.text = "Play/stop"
            }
            R.id.nav_status_info -> {
                if(update == "updated") {
                    list_view.visibility = View.GONE
                    light_name.text = ""
                    light_status.text = ""
                    light_on_off.text = ""
                    var temp = ""
                    for(i in 0 until json_arr.length()) {
                        val id_light = json_arr.getJSONObject(i).getString("id_light")
                        val status = json_arr.getJSONObject(i).getString("status")
                        temp = temp + "id_light: $id_light, status: $status \n"
                    }
                    light_lable.text = temp
                }
            }
            R.id.logout -> {
                confimDialog(this).show(fragmentManager, "logout")
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
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

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val obj = JSONObject(result)
            val light_status: String = obj.getString("light_status")
            light = obj
            if(light_status != "cannot access") {
                update = "updated"
                light_lable.text = "Dữ liệu đã được cập nhật"
                Toast.makeText(this@Main3Activity, "Đã cập nhật dữ liệu", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
