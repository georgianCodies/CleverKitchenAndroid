package com.mdev.cleverkitchenandroid.fragments.addrecipe

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mdev.cleverkitchenandroid.FirebaseStorageManager
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase


@Suppress("DEPRECATION")
class AddRecipeFragment : Fragment() {

    private lateinit var recipeName: TextView
    private lateinit var ingredients: TextView
    private lateinit var description: TextView
    private lateinit var video: Button
    private var mStorageRef: StorageReference? = null

    private val pickImage = 100
    private var imageUri: Uri? = null

    //var part_image: String? = null


    var filePath: ValueCallback<Array<Uri>>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)

        val submitButton = view.findViewById<Button>(R.id.submitButton);

        recipeName = view.findViewById<TextView>(R.id.recipeNameEditText);
        ingredients = view.findViewById<TextView>(R.id.ingredientsEditText)
        description = view.findViewById<TextView>(R.id.descriptionEditText)
        video = view.findViewById<Button>(R.id.videoInputButton);
        mStorageRef = FirebaseStorage.getInstance().getReference()
//        firebaseDatabase = FirebaseDatabase.getInstance().getReference("recipeImages")
//        firebaseDatabase.child("testing").setValue("valuye").addOnSuccessListener {
//            Log.d("creat","successful")
//        }.addOnFailureListener{
//            Log.d("error",it.toString())
//        }
        var isAllFieldsChecked = false;
        var imgURI = Uri.parse("");
        video.setOnClickListener {
            println("upload image button clicked!")
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)

        }

        submitButton.setOnClickListener(View.OnClickListener { // store the returned value of the dedicated function which checks
            // whether the entered data is valid or if any fields are left blank.
            isAllFieldsChecked = checkAllFields()

            // the boolean variable turns to be true then
            // only the user must be proceed to the activity2
            if (isAllFieldsChecked) {

                // initialise db
                val databaseClass = CleverKitchenDatabase(requireActivity())
                val sharedPreferences =
                    activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
                val emailId = sharedPreferences?.getString("emailId", "")

                val imgURI = video.tag as Uri?
                if(imgURI == null){
                    Toast.makeText(requireContext(),"Please select image first",Toast.LENGTH_SHORT).show()
                }else{
                   val uploadedURI =  FirebaseStorageManager().uploadImage(requireContext(),imgURI){ imageUri ->
                       Log.d("Add recipe- uploaded",imageUri.toString())
                       //insertion
                       val insertRecipe = databaseClass.insertRecipe(recipeName.text.toString(),
                           ingredients.text.toString(),
                           description.text.toString(),
                           imageUri.toString(),
                           emailId)
                       println(insertRecipe.toString())
                       Log.d("insert", insertRecipe.toString())
                   }

                }



                view.findNavController().navigate(R.id.action_addRecipeFragment_to_homeFragment)
            }
        })

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            println(imageUri);
            Log.d("image uri",imageUri.toString())
            video.setTag(imageUri)
            //imageView.setImageURI(imageUri)
        }


    }




    private fun checkAllFields(): Boolean {
        if (recipeName.length() === 0) {
            recipeName.error = "Recipe Name is required"
            return false
        }
        if (ingredients.length() === 0) {
            ingredients.error = "Ingredients is required"
            return false
        }
        if (description.length() === 0) {
            description.error = "Description is required"
            return false
        }
        // after all validation return true.
        return true
    }



}