package com.example.mypocket

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "MyPocketDB", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE users(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE, " +
                    "password TEXT, " +
                    "email TEXT, " +
                    "full_name TEXT, " +
                    "address TEXT, " +
                    "phone TEXT, " +
                    "gender TEXT, " +
                    "dob TEXT)"
        )
        db.execSQL(
            "CREATE TABLE balances(username TEXT PRIMARY KEY, balance REAL DEFAULT 0, " +
                    "FOREIGN KEY(username) REFERENCES users(username))"
        )
        db.execSQL(
            "CREATE TABLE transactions(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT, type TEXT, amount REAL, description TEXT, date TEXT, " +
                    "FOREIGN KEY(username) REFERENCES users(username))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS balances")
        db.execSQL("DROP TABLE IF EXISTS transactions")
        onCreate(db)
    }

    fun insertUser(
        username: String,
        password: String,
        email: String,
        fullName: String,
        address: String,
        phone: String,
        gender: String,
        dob: String
    ): Boolean {
        val db = writableDatabase

        // Check if the username already exists
        val cursor = db.rawQuery("SELECT username FROM users WHERE username = ?", arrayOf(username.trim()))
        if (cursor.moveToFirst()) {
            cursor.close()
            return false // Username already exists
        }
        cursor.close()

        val cv = ContentValues().apply {
            put("username", username.trim())
            put("password", password.trim())
            put("email", email.trim())
            put("full_name", fullName.trim())
            put("address", address.trim())
            put("phone", phone.trim())
            put("gender", gender.trim())
            put("dob", dob.trim())
        }

        val userInserted = db.insert("users", null, cv) != -1L
        if (userInserted) {
            val cvBalance = ContentValues().apply {
                put("username", username.trim())
                put("balance", 0.0)
            }
            db.insert("balances", null, cvBalance)
        }

        return userInserted
    }

    fun getUserProfile(username: String): Map<String, String?> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT full_name, email, address, phone, gender, dob FROM users WHERE username=?",
            arrayOf(username)
        )

        val profile = mutableMapOf<String, String?>()
        if (cursor.moveToFirst()) {
            profile["full_name"] = cursor.getString(0)
            profile["email"] = cursor.getString(1)
            profile["address"] = cursor.getString(2)
            profile["phone"] = cursor.getString(3)
            profile["gender"] = cursor.getString(4)
            profile["dob"] = cursor.getString(5)
        }
        cursor.close()
        return profile
    }

    fun checkLogin(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE username=? AND password=?",
            arrayOf(username.trim(), password.trim())
        )
        val isValid = cursor.moveToFirst()
        cursor.close()
        return isValid
    }

    fun getEmail(username: String): String? {
        val db = readableDatabase
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
        val db = writableDatabase
        val cv = ContentValues()
        cv.put("password", newPassword.trim())
        return db.update("users", cv, "username=?", arrayOf(username.trim())) > 0
    }

    fun addMoney(username: String, amount: Double): Boolean {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.execSQL("UPDATE balances SET balance = balance + ? WHERE username = ?", arrayOf(amount, username))
            val cv = ContentValues()
            cv.put("username", username)
            cv.put("type", "add")
            cv.put("amount", amount)
            cv.put("description", "Added money")
            cv.put("date", System.currentTimeMillis().toString())
            db.insert("transactions", null, cv)
            db.setTransactionSuccessful()
            return true
        } catch (e: Exception) {
            return false
        } finally {
            db.endTransaction()
        }
    }

    fun sendMoney(fromUser: String, toUser: String, amount: Double): Boolean {
        val db = writableDatabase
        db.beginTransaction()

        try {
            if (fromUser.trim() == toUser.trim()) {
                Log.e("SendMoney", "Cannot send money to yourself.")
                return false
            }

            val cursor = db.rawQuery("SELECT * FROM users WHERE username=?", arrayOf(toUser.trim()))
            if (!cursor.moveToFirst()) {
                cursor.close()
                return false
            }
            cursor.close()

            val balCursor = db.rawQuery("SELECT balance FROM balances WHERE username=?", arrayOf(fromUser.trim()))
            if (balCursor.moveToFirst()) {
                val balance = balCursor.getDouble(0)
                if (balance < amount) {
                    balCursor.close()
                    return false
                }
            } else {
                balCursor.close()
                return false
            }
            balCursor.close()

            // Update balances
            db.execSQL("UPDATE balances SET balance = balance - ? WHERE username = ?", arrayOf(amount, fromUser.trim()))
            db.execSQL("UPDATE balances SET balance = balance + ? WHERE username = ?", arrayOf(amount, toUser.trim()))

            // Log transactions
            val cvSender = ContentValues().apply {
                put("username", fromUser.trim())
                put("type", "send")
                put("amount", amount)
                put("description", "Sent to $toUser")
                put("date", System.currentTimeMillis().toString())
            }
            db.insert("transactions", null, cvSender)

            val cvReceiver = ContentValues().apply {
                put("username", toUser.trim())
                put("type", "receive")
                put("amount", amount)
                put("description", "Received from $fromUser")
                put("date", System.currentTimeMillis().toString())
            }
            db.insert("transactions", null, cvReceiver)

            db.setTransactionSuccessful()
            return true
        } catch (e: Exception) {
            Log.e("SendMoney", "Error: ${e.message}")
            return false
        } finally {
            db.endTransaction()
        }
    }


    fun getTransactions(username: String): List<String> {
        val transactions = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT type, amount, description, date FROM transactions WHERE username=? ORDER BY date DESC",
            arrayOf(username)
        )
        while (cursor.moveToNext()) {
            val type = cursor.getString(0)
            val amount = cursor.getDouble(1)
            val desc = cursor.getString(2)
            val date = cursor.getString(3)
            transactions.add("$date: [$type] $desc - $$amount")
        }
        cursor.close()
        return transactions
    }

    fun getRecentTransactions(username: String, limit: Int = 5): List<String> {
        val transactions = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT type, amount, description, date FROM transactions WHERE username=? ORDER BY date DESC LIMIT ?",
            arrayOf(username, limit.toString())
        )
        while (cursor.moveToNext()) {
            val type = cursor.getString(0)
            val amount = cursor.getDouble(1)
            val desc = cursor.getString(2)
            val date = cursor.getString(3)
            transactions.add("$date: [$type] $desc - $$amount")
        }
        cursor.close()
        return transactions
    }


    fun getBalance(username: String): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT balance FROM balances WHERE username=?", arrayOf(username))
        return if (cursor.moveToFirst()) {
            val bal = cursor.getDouble(0)
            cursor.close()
            bal
        } else {
            cursor.close()
            0.0
        }
    }
}
