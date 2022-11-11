package com.mdev.cleverkitchenandroid

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment


class AddRecipeFragment : Fragment() {

    private lateinit var recipeName: TextView
    private lateinit var ingredients: TextView
    private lateinit var description: TextView
    private lateinit var video: TextView


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
        video = view.findViewById<TextView>(R.id.videoInputText);

        var isAllFieldsChecked = false


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