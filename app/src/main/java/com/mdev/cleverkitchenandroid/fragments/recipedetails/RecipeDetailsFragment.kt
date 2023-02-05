package com.mdev.cleverkitchenandroid.fragments.recipedetails

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import kotlinx.android.synthetic.main.fragment_recipe_details.*

class RecipeDetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_details, container, false)
        val saveButton = view.findViewById<Button>(R.id.saveButton);

        var isAllFieldsChecked = false;

        saveButton.setOnClickListener(View.OnClickListener {

            isAllFieldsChecked = checkAllFields()

            if (isAllFieldsChecked) {

                // initialise db
                val databaseClass = CleverKitchenDatabase(requireActivity())
                val sharedPreferences =
                    activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
                //insertion
                val insertRecipe = databaseClass.updateRecipeDetails(
                    requireArguments().getString("recipe_id").toString(),
                    notes.text.toString()
                )
                println(insertRecipe.toString())
            }
        })
        return view
    }
    //Set recipe here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.recipe_name).text = requireArguments().getString("recipe_name")
        view.findViewById<TextView>(R.id.description).text = requireArguments().getString("description")
        view.findViewById<TextView>(R.id.notes).text = requireArguments().getString("notes")
        view.findViewById<TextView>(R.id.recipe_sub_name).text = requireArguments().getString("recipe_name") + ": A classic Indian dish"
        view.findViewById<TextView>(R.id.chip).text = requireArguments().getString("chip")
        view.findViewById<TextView>(R.id.how_to).text = "How to make " + requireArguments().getString("recipe_name")+"?"

    }

    private fun checkAllFields(): Boolean {
        if (notes.length() === 0) {
            notes.error = "Notes is empty"
        }
        if (notes.length() === 0) {
            return false
        }
        return true
    }
}