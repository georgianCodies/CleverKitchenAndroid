package com.mdev.cleverkitchenandroid.database

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context

class UserDetailsDatabase(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $USER_DETAILS_TABLE($COL_EMAIL_ID TEXT PRIMARY KEY , $COL_USER_NAME TEXT, $COL_PASSWORD TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $USER_DETAILS_TABLE")
    }

    fun insertUser(email:String?, username: String?, password: String?): Boolean {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_EMAIL_ID,email)
        contentValues.put(COL_USER_NAME, username)
        contentValues.put(COL_PASSWORD, password)

        val result = sqLiteDatabase.insert(USER_DETAILS_TABLE, null, contentValues)
        sqLiteDatabase.close()
        return !result.equals(-1)
    }

    fun checkEmail(email: String): Boolean {
        val sqLiteDatabase = this.writableDatabase
        val result =
            sqLiteDatabase.rawQuery("SELECT * FROM $USER_DETAILS_TABLE WHCOL_$COL_EMAIL_ID=?", arrayOf(email))
        sqLiteDatabase.close()
        return !result.equals(-1)
    }

    fun checkLogin(email: String, password: String): Boolean {
        val sqLiteDatabase = this.readableDatabase
        val result = sqLiteDatabase.rawQuery(
            "SELECT * FROM $USER_DETAILS_TABLE WHERE $COL_EMAIL_ID=? AND $COL_PASSWORD=?",
            arrayOf(email, password)
        )
        sqLiteDatabase.close()
        return !result.equals(-1)
    }

    companion object{
        private const val DATABASE_NAME = "clever_kitchen.db"
        private const val DATABASE_VERSION = 1
        private const val USER_DETAILS_TABLE = "user"
        private const val COL_USER_NAME = "user_name"
        private const val COL_PASSWORD = "password"
        private const val COL_EMAIL_ID = "email_id"

    }
}