package com.mdev.cleverkitchenandroid

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.io.File

class AddRecipeFragment : Fragment() {

    private val sharedPrefFile = "kotlinsharedpreference"
    private lateinit var recipeName: TextView
    private lateinit var ingredients: TextView
    private lateinit var description: TextView
    private lateinit var video: TextView

    var part_image: String? = null


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
        //video = view.findViewById<Button>(R.id.videoInputButton);

        var isAllFieldsChecked = false

        video.setOnClickListener {
            println("clicked on video upload!");
        }


        submitButton.setOnClickListener(View.OnClickListener { // store the returned value of the dedicated function which checks
            // whether the entered data is valid or if any fields are left blank.
            isAllFieldsChecked = checkAllFields()

            // the boolean variable turns to be true then
            // only the user must be proceed to the activity2
            if (isAllFieldsChecked) {
               // val i = Intent(this@AddRecipeFragment, AddRecipeFragment::class.java)
               // startActivity(i)
                println("all feild are available")
                println(recipeName.text)
                println(ingredients.text)
                println(description.text)

                val sharedPreferences: SharedPreferences =
                    context?.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)!!
                    //val id:Int = Integer.parseInt(inputId.text.toString())
                   // val name:String = inputName.text.toString()
                val editor:SharedPreferences.Editor =  sharedPreferences.edit()

                editor.putString("recipeName",recipeName.text.toString())
                editor.putString("ingredients",ingredients.text.toString())
                editor.putString("description",description.text.toString())

                editor.apply()
                editor.commit()

                println("Saved data is:")
                val savedRecipeName = sharedPreferences.getString("recipeName","no values")
                println(savedRecipeName)
            }
        })


        return view
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