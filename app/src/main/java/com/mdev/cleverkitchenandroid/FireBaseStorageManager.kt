@file:Suppress("DEPRECATION")

package com.mdev.cleverkitchenandroid


import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class FirebaseStorageManager {
    private var mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var progressBar: ProgressDialog
    fun uploadImage(context: Context, imageFileUri: Uri,emailId:String, callback: (Any) -> Unit) {
        progressBar = ProgressDialog(context)
        progressBar.setMessage("Please wait, image being uploaded")
        progressBar.show()
        val date = Date()
        val uploadTask = mStorageRef.child("recipeImages/${emailId}/${date}.png").putFile(imageFileUri)
        uploadTask.addOnSuccessListener {
            Log.e("Firebase", it.toString())
            progressBar.dismiss()
             mStorageRef.child("recipeImages/${emailId}/${date}.png").downloadUrl.addOnSuccessListener {

                callback(it.toString())
            }.addOnFailureListener {
                 progressBar.dismiss()
            }
        }.addOnFailureListener {
            Log.e("Firebase", "Image Upload failed")
            progressBar.dismiss()
        }
    }

    fun retrieveImage(context: Context, imageFileUri: Uri) {
        progressBar = ProgressDialog(context)
        progressBar.setMessage("Please wait, Retrieving your recipes")
        progressBar.show()
        val date = Date()


    }
}