package com.example.duc25.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import es.dmoral.toasty.Toasty

@SuppressLint("ValidFragment")
/**
 * Created by duc25 on 3/26/2018.
 */

class Confim(val myContext: Context): DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder  = AlertDialog.Builder(activity).apply {
            setTitle("Logout")
            setMessage("Choose")

            setPositiveButton("Agree", {dialog, id ->
                val obj = Db("", myContext)
                obj.delete()
                val intent = Intent(myContext,MainActivity::class.java)
                startActivity(intent)
                Toasty.success(myContext, "Đã đăng xuất!", Toast.LENGTH_SHORT, true).show();
                (myContext as Activity).finish()
            })

            setNegativeButton("Cancel", {dialog, id ->

            })
        }

        return builder.create()
    }
}