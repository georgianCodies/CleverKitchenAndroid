package com.mdev.cleverkitchenandroid.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.mdev.cleverkitchenandroid.model.Recipe


class RecipeDatabase(context:Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "clever_kitchen.db"
        private const val DATABASE_VERSION = 1
        private const val RECIPE_TABLE = "recipe_details"
        private const val USER_DETAILS_TABLE = "user"
        private const val COL_RECIPE_ID = "recipe_id"
        private const val COL_RECIPE_NAME = "recipe_name"
        private const val COL_INGREDIENTS = "ingredients"
        private const val COL_DESCRIPTION = "description"
        private const val COL_IMG_LOCATION = "img_location"
        private const val COL_EMAIL_ID = "email_id"

    }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("PRAGMA foreign_keys = ON")
        db.execSQL("CREATE TABLE $RECIPE_TABLE(" +
                "$COL_RECIPE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COL_RECIPE_NAME TEXT ," +
                "$COL_INGREDIENTS TEXT ," +
                "$COL_DESCRIPTION TEXT , " +
                "$COL_IMG_LOCATION TEXT, " +
                "$COL_EMAIL_ID TEXT, " +
                " FOREIGN KEY($COL_EMAIL_ID) REFERENCES $USER_DETAILS_TABLE($COL_EMAIL_ID))")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                db.execSQL("DROP TABLE IF EXISTS $RECIPE_TABLE")
    }

    fun insert(recipe_name: String?, ingredients: String?, description: String?,img_location:String?, email_id:String?): Boolean {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_RECIPE_NAME, recipe_name)
        contentValues.put(COL_INGREDIENTS, ingredients)
        contentValues.put(COL_DESCRIPTION, description)
        contentValues.put(COL_IMG_LOCATION, img_location)
        contentValues.put(COL_EMAIL_ID, email_id)

        val result = sqLiteDatabase.insert(RECIPE_TABLE, null, contentValues)
        Log.d("recipeList", result.toString())
        sqLiteDatabase.close()
        return !result.equals(-1)
    }

    fun getRecipeDetails(email_id: String): ArrayList<Recipe> {
        val sqliteDatabase = this.writableDatabase
        val cursor =  sqliteDatabase.rawQuery("SELECT * FROM $RECIPE_TABLE WHERE $COL_EMAIL_ID=?", arrayOf(email_id))
        val recipeList: ArrayList<Recipe> = ArrayList()

        if (cursor.moveToFirst()) {
            do {
                recipeList.add(
                    Recipe(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                    )
                )
            } while (cursor.moveToNext())

        }
        sqliteDatabase.close()
        Log.d("recipeList", recipeList.toString())
        return recipeList;

    }
}