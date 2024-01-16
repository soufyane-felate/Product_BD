package com.example.felatesoufyaneefm

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(context: Context) : SQLiteOpenHelper(context, "DB_Product", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS info(id INTEGER PRIMARY KEY, name TEXT, price REAL, image TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS info")
        onCreate(db)
    }

    fun addData(id: Int, name: String, price: Double, image: String): Long {
        val writableDb = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("id", id)
            put("name", name)
            put("price", price)
            put("image", image)
        }
        return writableDb.insert("info", null, contentValues)
    }





    @SuppressLint("Range")
    fun getAllPc(): List<Product> {
        val pcList = mutableListOf<Product>()
        val query = "SELECT * FROM info"
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(query, null)
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndex("id"))
                    val name = it.getString(it.getColumnIndex("name"))
                    val price = it.getDouble(it.getColumnIndex("price"))
                    val image = it.getString(it.getColumnIndex("image"))
                    val pc = Product(id, name, price, image)
                    pcList.add(pc)
                } while (it.moveToNext())
            }
        }
        return pcList
    }
}