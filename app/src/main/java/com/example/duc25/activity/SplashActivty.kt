package com.example.duc25.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_splash_activty.*

class SplashActivty : AppCompatActivity() {

    var width = 0f
    var height = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_activty)
        setSize()
        setUI()
        Handler().postDelayed({
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        },500)
    }

    private fun setUI(){
        val textView = TextView(this)
        val processBar  =ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)

        textView.text = "Home"
        textView.textSize = 25f
        textView.setTextColor(Color.WHITE)
        textView.typeface = Typeface.DEFAULT_BOLD

        textView.measure(0, 0)
        processBar.measure(0, 0)

        rl_splash.addView(textView)
        rl_splash.addView(processBar)

        textView.x = width*50 - textView.measuredWidth/2
        textView.y = height*40

        processBar.x = width*50 - processBar.measuredWidth/2
        processBar.y = height*45
        processBar.isIndeterminate = true
        processBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_ATOP)
    }

    private fun setSize(){
        val manager = windowManager.defaultDisplay
        val size = Point()
        manager.getSize(size)
        width = size.x/100f
        height = size.y/100f
    }
}
