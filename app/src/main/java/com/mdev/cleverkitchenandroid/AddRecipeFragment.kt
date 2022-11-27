package com.mdev.cleverkitchenandroid

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import com.mdev.cleverkitchenandroid.model.Recipe

class AddRecipeFragment : Fragment() {

    private val sharedPrefFile = "kotlinsharedpreference"
    private lateinit var recipeName: TextView
    private lateinit var ingredients: TextView
    private lateinit var description: TextView


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

//        video.setOnClickListener {
//            println("clicked on video upload!");
//        }


        submitButton.setOnClickListener(View.OnClickListener { // store the returned value of the dedicated function which checks
            // whether the entered data is valid or if any fields are left blank.
            isAllFieldsChecked = checkAllFields()

            // the boolean variable turns to be true then
            // only the user must be proceed to the activity2
            if (isAllFieldsChecked) {

                // initialise db
                val databaseClass = CleverKitchenDatabase(requireActivity())

                //insertion
                val insertRecipe = databaseClass.insertRecipe(recipeName.text.toString(),ingredients.text.toString(),description.text.toString(),"../images","ch")
                Log.d("insert", insertRecipe.toString())

                //getRecipe Details
                val recipes:ArrayList<Recipe> = databaseClass.getRecipeDetails("ch")
                Log.d("recipe",recipes.toString());

                for(element in recipes){
                    Log.d("element-email",element.email_id)
                    Log.d("element-item",element.recipe_name)
                }
//                view.findNavController().navigate(R.id.action_addRecipeFragment_to_homeFragment)
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