package com.example.duc25.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class Db(private var data: String, var context: Context): SQLiteOpenHelper(context, "userdata.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            val sql = "CREATE TABLE users(id int primary key, data text)"
            db?.execSQL(sql)
        }catch (e: IllegalStateException){
            Toast.makeText(context, "errSQL", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insert(){
        val dbWrite = this.writableDatabase
        val value = ContentValues()
        value.put("id", 1)
        value.put("data", data)
        dbWrite.insert("users", null, value)
    }

    fun update(){
        val dbWrite = this.writableDatabase
        val value = ContentValues()
        value.put("data", data)
        dbWrite.update("users", value, "id=?", arrayOf("1"))
    }

    @SuppressLint("Recycle")
    fun read(): String{
        val dbRead = readableDatabase
        val cursor = dbRead.rawQuery("SELECT data FROM users", null)
        cursor.moveToFirst()
        return cursor.getString(0)
    }

    fun delete(){
        val dbWrite = this.writableDatabase
        dbWrite.delete("users", "id=1", null)
    }
}