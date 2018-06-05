package com.example.duc25.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CompoundButton
import android.widget.Toast
import kotlinx.android.synthetic.main.field_listview.view.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Created by duc25 on 1/16/2018.
 */

class CustomAdapter (val context: Context, val layout: Int, val array: List<FieldValue>, val lable: String, val token: String): BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layout_dong = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view: View = layout_dong.inflate(layout,null)

        val arr: FieldValue = array[p0]

        view.textView.text = arr.field1
        view.textView5.text = arr.field2

        if(view.textView5.text == "On" || view.textView5.text == "Play"){
            view.switch1.isChecked = true
        }else{
            view.switch1.isChecked = false
        }

        view.switch1.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onCheckedChanged(buttonView:CompoundButton, isChecked:Boolean) {
                if (view.switch1.isChecked) {
                    when(lable){
                        "đèn" -> {requestURL().execute("https://techitvn.com/home/api/remote.php?namedevice=light&id=" + arr.field1 + "&status=1&token=" + token)
                            view.textView5.text = "On"
                            //Toast.makeText(context, "Bật " + lable + " " + arr.field1, Toast.LENGTH_SHORT).show()
                        }
                        "bài" -> {Toast.makeText(context, "Play " + lable + " " + arr.field1, Toast.LENGTH_SHORT).show();view.textView5.text = "Play"}
                        else -> {
                            view.textView5.text = "On"
                            Toast.makeText(context,"Bật " + lable + " " + arr.field1, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    when(lable){
                        "đèn" -> {requestURL().execute("https://techitvn.com/home/api/remote.php?namedevice=light&id=" + arr.field1 + "&status=0&token=" + token)
                            view.textView5.text = "Off"
                            //Toast.makeText(context,"Tắt " + lable + " " + arr.field1, Toast.LENGTH_SHORT).show()
                        }
                        "bài" -> {Toast.makeText(context, "Stop " + lable + " " + arr.field1, Toast.LENGTH_SHORT).show();view.textView5.text = "Stop"}
                        else -> {
                            view.textView5.text = "Off"
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
            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show()
        }
    }
}