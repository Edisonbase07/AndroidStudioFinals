package com.example.mypocket  // ✅ replace with your actual package name

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "MyPocketDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE, password TEXT, email TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun insertUser(username: String, password: String, email: String): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("username", username)
        cv.put("password", password)
        cv.put("email", email)
        return db.insert("users", null, cv) != -1L
    }

    fun checkLogin(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE username=? AND password=?",
            arrayOf(username, password)
        )
        val result = cursor.count > 0
        cursor.close()
        return result
    }

    fun getEmail(username: String): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT email FROM users WHERE username=?", arrayOf(username))
        return if (cursor.moveToFirst()) {
            val email = cursor.getString(0)
            cursor.close()
            email
        } else {
            cursor.close()
            null
        }
    }

    fun updatePassword(username: String, newPassword: String): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("password", newPassword)
        return db.update("users", cv, "username=?", arrayOf(username)) > 0
    }
}
