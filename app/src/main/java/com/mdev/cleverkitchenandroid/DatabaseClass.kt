package com.mdev.cleverkitchenandroid

import android.database.sqlite.SQLiteOpenHelper
import com.mdev.cleverkitchenandroid.DatabaseClass
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context

class DatabaseClass(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE user(email TEXT PRIMARY KEY , username TEXT, password TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS user")
    }

    fun Insert(email:String?, username: String?, password: String?): Boolean {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("email",email)
        contentValues.put("username", username)
        contentValues.put("password", password)

        val result = sqLiteDatabase.insert("user", null, contentValues)
        return if (result == -1L) {
            false
        } else {
            true
        }
    }

    fun CheckEmail(email: String): Boolean {
        val sqLiteDatabase = this.writableDatabase
        val cursor =
            sqLiteDatabase.rawQuery("SELECT * FROM user WHERE email=?", arrayOf(email))
        return if (cursor.count > 0) {
            false
        } else {
            true
        }
    }

    fun CheckLogin(email: String, password: String): Boolean {
        val sqLiteDatabase = this.readableDatabase
        val cursor = sqLiteDatabase.rawQuery(
            "SELECT * FROM user WHERE email=? AND password=?",
            arrayOf(email, password)
        )
        return if (cursor.count > 0) {
            true
        } else {
            false
        }
    }

    companion object {
        const val DATABASE_NAME = "login.db"
    }
}