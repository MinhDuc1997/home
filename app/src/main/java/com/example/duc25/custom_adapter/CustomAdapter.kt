package com.example.duc25.activity

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.AsyncTask
import android.support.v4.graphics.drawable.DrawableCompat
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.duc25.config.UriApi
import com.tapadoo.alerter.Alerter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.field_listview.view.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Created by duc25 on 1/16/2018.
 */

class CustomAdapter (val context: Main3Activity, val layout: Int, val array: List<FieldValue>, val lable: String, val token: String): BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layout_dong = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view: View = layout_dong.inflate(layout,null)

        val arr: FieldValue = array[p0]

        val textView = TextView(context)
        val textView5 = TextView(context)
        val switch1 = Switch(context)

        textView.text = arr.field1
        textView5.text = arr.field2

        textView.textSize = 18f
        textView5.textSize = 18f

        textView.typeface = Typeface.DEFAULT_BOLD
        textView5.typeface = Typeface.DEFAULT_BOLD

        switch1.showText = false

        textView.measure(0,0)
        textView5.measure(0,0)
        switch1.measure(0, 0)

        view.rl_value.addView(textView)
        view.rl_value.addView(textView5)
        view.rl_value.addView(switch1)

        textView.x = context.width*5
        textView5.x = context.width*50 - textView5.measuredWidth/2
        switch1.x = context.width*100 - switch1.measuredWidth - context.width*5

        val states = arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked))

        val thumbColors = intArrayOf(Color.GRAY, Color.parseColor("#fdc51162"))
        val trackColors = intArrayOf(Color.GRAY, Color.parseColor("#fdc51162"))
        DrawableCompat.setTintList(DrawableCompat.wrap(switch1.thumbDrawable), ColorStateList(states, thumbColors))
        DrawableCompat.setTintList(DrawableCompat.wrap(switch1.trackDrawable), ColorStateList(states, trackColors))

        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, (context.height*2).toInt(), 0, 0)
        params.width = textView.measuredWidth
        textView.layoutParams = params
        params.width = textView5.measuredWidth
        textView5.layoutParams = params
        params.width = switch1.measuredWidth
        switch1.layoutParams = params

        if(textView5.text == "On" || textView5.text == "Play"){
            switch1.isChecked = true
        }else{
            switch1.isChecked = false
        }

        switch1.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onCheckedChanged(buttonView:CompoundButton, isChecked:Boolean) {
                if (switch1.isChecked) {
                    when(lable){
                        "đèn" -> {requestURL().execute(UriApi(null,null, arr.field1.toInt(), 1).uriAPiRemote + token)
                            textView5.text = "On"
                            //Toast.makeText(context, "Bật " + lable + " " + arr.field1, Toast.LENGTH_SHORT).show()
                        }
                        "bài" -> {Toast.makeText(context, "Play " + lable + " " + arr.field1, Toast.LENGTH_SHORT).show(); textView5.text = "Play"}
                        else -> {
                            textView5.text = "On"
                            Toast.makeText(context,"Bật " + lable + " " + arr.field1, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    when(lable){
                        "đèn" -> {requestURL().execute(UriApi(null,null, arr.field1.toInt(), 0).uriAPiRemote + token)
                            textView5.text = "Off"
                            //Toast.makeText(context,"Tắt " + lable + " " + arr.field1, Toast.LENGTH_SHORT).show()
                        }
                        "bài" -> {Toast.makeText(context, "Stop " + lable + " " + arr.field1, Toast.LENGTH_SHORT).show(); textView5.text = "Stop"}
                        else -> {
                            textView5.text = "Off"
                            Toast.makeText(context,"Tắt " + lable + " " + arr.field1, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
        return view
    }

    override fun getItem(p0: Int): Any = null!!

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return array.size
    }

    @SuppressLint("StaticFieldLeak")
    inner class requestURL : AsyncTask<String, Void, String>() {

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
            val jsonObj = JSONObject(result)
            val status = jsonObj.getString("status")
            if(status == "true"){
                Alerter.create(context)
                        .setTitle("Done")
                        .setText("")
                        .setBackgroundColorInt(Color.parseColor("#fdc51162"))
                        .enableSwipeToDismiss()
                        .show()
                //Toasty.success(context, "Done", Toast.LENGTH_SHORT, true).show()
            }else{
                Toasty.error(context, "False", Toast.LENGTH_SHORT, true).show()
            }
        }
    }
}