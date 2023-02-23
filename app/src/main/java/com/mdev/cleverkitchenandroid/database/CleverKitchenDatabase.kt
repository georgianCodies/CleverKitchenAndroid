package com.mdev.cleverkitchenandroid.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.mdev.cleverkitchenandroid.model.Recipe
import com.mdev.cleverkitchenandroid.model.User

class CleverKitchenDatabase(context:Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "clever_kitchen_new1.db"
        private const val DATABASE_VERSION = 1

        //shopping-list table
        private const val SHOPPING_LIST_TABLE = "shopping_list"
        private const val COL_ITEMS = "items"

        //recipe-details table
        private const val RECIPE_TABLE = "recipe_details"
        private const val COL_RECIPE_ID = "recipe_id"
        private const val COL_RECIPE_NAME = "recipe_name"
        private const val COL_INGREDIENTS = "ingredients"
        private const val COL_DESCRIPTION = "description"
        private const val COL_NOTES = "notes"
        private const val COL_IMG_LOCATION = "img_location"
        private const val COL_IS_FAVORITE = "is_favorite"
        private const val COL_CREATED_ON = "created_on"

        //user-details table
        private const val USER_DETAILS_TABLE = "user"
        private const val COL_USER_NAME = "user_name"
        private const val COL_FIRST_NAME = "first_name"
        private const val COL_LAST_NAME = "last_name"
        private const val COL_PASSWORD = "password"
        private const val COL_EMAIL_ID = "email_id"
        private const val COL_PHONE = "phone"
        private const val COL_USER_IMAGE = "user_img_location"

    }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("PRAGMA foreign_keys = ON")
        db.execSQL("CREATE TABLE $RECIPE_TABLE(" +
                "$COL_RECIPE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COL_RECIPE_NAME TEXT, " +
                "$COL_INGREDIENTS TEXT, " +
                "$COL_DESCRIPTION TEXT, " +
                "$COL_NOTES TEXT , " +
                "$COL_IMG_LOCATION TEXT, " +
                "$COL_EMAIL_ID TEXT, " +
                "$COL_IS_FAVORITE INT DEFAULT 0, " +
                "$COL_CREATED_ON TEXT, " +
                " FOREIGN KEY($COL_EMAIL_ID) REFERENCES $USER_DETAILS_TABLE($COL_EMAIL_ID))")
        db.execSQL("CREATE TABLE ${USER_DETAILS_TABLE}(${COL_EMAIL_ID} TEXT PRIMARY KEY ,$COL_FIRST_NAME TEXT,$COL_LAST_NAME TEXT, $COL_PHONE TEXT, $COL_USER_IMAGE TEXT, $COL_USER_NAME TEXT, " +
                "$COL_PASSWORD TEXT)")
        db.execSQL("CREATE TABLE ${SHOPPING_LIST_TABLE}($COL_ITEMS TEXT, $COL_EMAIL_ID TEXT, " +
                "FOREIGN KEY($COL_EMAIL_ID) REFERENCES $USER_DETAILS_TABLE($COL_EMAIL_ID))")

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $RECIPE_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $USER_DETAILS_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $SHOPPING_LIST_TABLE")
    }

    fun insertRecipe(recipe_name: String?, ingredients: String?, description: String?,img_location:String?, email_id:String?, created_on:String?): Boolean {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_RECIPE_NAME, recipe_name)
        contentValues.put(COL_INGREDIENTS, ingredients)
        contentValues.put(COL_NOTES, "")
        contentValues.put(COL_DESCRIPTION, description)
        contentValues.put(COL_IMG_LOCATION, img_location)
        contentValues.put(COL_EMAIL_ID, email_id)
        contentValues.put(COL_CREATED_ON, created_on)

        val cursor = sqLiteDatabase.insert(RECIPE_TABLE, null, contentValues)
        Log.d("recipeList", cursor.toString())
        return !cursor.equals(-1)
    }

    fun toggleFavorite(recipe_id: String, is_fav: Int): Boolean {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_IS_FAVORITE, is_fav)

        val cursor = sqLiteDatabase.update(RECIPE_TABLE, contentValues, "$COL_RECIPE_ID=?", arrayOf(recipe_id))
        Log.d("updated recipe", cursor.toString())
        return !cursor.equals(-1)
    }

    fun getRecipeDetails(email_id: String?, favorites: String? = null): ArrayList<Recipe> {
        val sqliteDatabase = this.readableDatabase
        Log.d("email in db", email_id.toString())

        var cursor: android.database.Cursor;
        if(favorites === null) {
            cursor =  sqliteDatabase.rawQuery("SELECT * FROM $RECIPE_TABLE WHERE $COL_EMAIL_ID=?", arrayOf(email_id))
        } else {
            cursor =  sqliteDatabase.rawQuery("SELECT * FROM $RECIPE_TABLE WHERE $COL_EMAIL_ID=? AND $COL_IS_FAVORITE=?", arrayOf(email_id, favorites))
        }

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
                        cursor.getString(6),
                        cursor.getInt(6)
                    )
                )
            } while (cursor.moveToNext())

        }
        Log.d("recipeList", recipeList.toString())
        return recipeList;
    }

    fun updateRecipeDetails(recipe_id: String, notes: String): Boolean {
        val sqliteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NOTES, notes)
        val cursor = sqliteDatabase.update(RECIPE_TABLE, contentValues,"$COL_RECIPE_ID=?", arrayOf(recipe_id))
        return cursor != -1
    }

    fun insertShoppingList(shoppingList:String?,email:String?): Boolean {
        val sqliteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ITEMS,shoppingList)
        contentValues.put(COL_EMAIL_ID,email)

        val hasShoppingList = sqliteDatabase.rawQuery("SELECT * FROM $SHOPPING_LIST_TABLE WHERE $COL_EMAIL_ID=?", arrayOf(email))
        Log.d("shoppingList", contentValues.toString())

        return if(hasShoppingList.count==0){
            val cursor = sqliteDatabase.insert(SHOPPING_LIST_TABLE, null, contentValues)
            !cursor.equals(-1)
        } else{
            val cursor = sqliteDatabase.update(SHOPPING_LIST_TABLE, contentValues,"$COL_EMAIL_ID=?", arrayOf(email))
            !cursor.equals(-1)
        }
    }

    fun getShoppingList(email: String): String {
        val sqliteDatabase = this.readableDatabase
        val cursor = sqliteDatabase.rawQuery("SELECT * FROM $SHOPPING_LIST_TABLE WHERE $COL_EMAIL_ID=?", arrayOf(email))
        cursor.moveToFirst()
        return if (cursor.count > 0){
            Log.d("shopping-list", cursor.getString(0))
            cursor.getString(0)
        }else{
            ""
        }
    }

    fun insertUser(email:String?,firstName:String?,lastName:String?,phone:String?,userImg:String?,username: String?, password: String?): Boolean {
        val sqliteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_EMAIL_ID,email)
        contentValues.put(COL_FIRST_NAME,firstName)
        contentValues.put(COL_LAST_NAME,lastName)
        contentValues.put(COL_PHONE,phone)
        contentValues.put(COL_USER_IMAGE,userImg)
        contentValues.put(COL_EMAIL_ID,email)
        contentValues.put(COL_USER_NAME, username)
        contentValues.put(COL_PASSWORD, password)
        Log.d("insert user", contentValues.toString())
        val cursor = sqliteDatabase.insert(USER_DETAILS_TABLE, null, contentValues)
        return !cursor.equals(-1)
    }

    fun deleteUser(email: String): Boolean {
        val sqliteDatabase = this.writableDatabase
        Log.d("email",email)
        val cursor  = sqliteDatabase.execSQL("DELETE FROM $USER_DETAILS_TABLE WHERE $COL_EMAIL_ID=?", arrayOf(email))
        return !cursor.equals(-1)
    }

    fun checkEmail(email: String): Boolean {
        val sqliteDatabase = this.writableDatabase
        val cursor = sqliteDatabase.rawQuery("SELECT * FROM $USER_DETAILS_TABLE WHERE $COL_EMAIL_ID=?", arrayOf(email))
        return !cursor.equals(-1)
    }

    fun getUser(email: String): User {
        val sqliteDatabase = this.readableDatabase
        val cursor = sqliteDatabase.rawQuery("SELECT * FROM $USER_DETAILS_TABLE WHERE $COL_EMAIL_ID=?", arrayOf(email))
        cursor.moveToFirst()
        Log.d("0",cursor.getString(0))
        Log.d("1",cursor.getString(1))
        Log.d("2",cursor.getString(2))
        Log.d("3",cursor.getString(3))
        Log.d("4",cursor.getString(4))
        Log.d("5",cursor.getString(5))
        Log.d("6",cursor.getString(6))

        val user = User(cursor.getString(1), cursor.getString(2), cursor.getString(5),cursor.getString(0),cursor.getString(6))
        Log.d("logged-in user", cursor.getString(0).toString())
        return user
    }

    fun updateUser(email: String,firstName:String,lastName:String): Boolean {
        Log.d("logged-in user", firstName)
        Log.d("logged-in email", email)
        val sqliteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_FIRST_NAME, firstName)
        contentValues.put(COL_LAST_NAME, lastName)
        Log.d("recipeList", contentValues.toString())
        val cursor = sqliteDatabase.update(USER_DETAILS_TABLE, contentValues,"$COL_EMAIL_ID=?", arrayOf(email))
        Log.d("logged-in email", cursor.toString())
        return cursor != -1
    }

    fun checkLogin(email: String, password: String): Boolean {
        val sqliteDatabase = this.readableDatabase
        val cursor = sqliteDatabase.rawQuery("SELECT * FROM $USER_DETAILS_TABLE WHERE ($COL_EMAIL_ID=? OR $COL_USER_NAME=?) AND $COL_PASSWORD=?", arrayOf(email,email, password))
        return cursor.count > 0
    }

    fun getUserEmail(id: String): String {
        val sqliteDatabase = this.readableDatabase
        val cursor = sqliteDatabase.rawQuery("SELECT * FROM $USER_DETAILS_TABLE WHERE $COL_EMAIL_ID=? OR $COL_USER_NAME=?", arrayOf(id,id))
        cursor.moveToFirst()
        return cursor.getString(0)
    }
}