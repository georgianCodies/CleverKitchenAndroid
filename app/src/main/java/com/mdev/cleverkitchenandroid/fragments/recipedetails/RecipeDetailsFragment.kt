package com.mdev.cleverkitchenandroid.fragments.recipedetails

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import kotlinx.android.synthetic.main.fragment_recipe_details.*

class RecipeDetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
                    requireArguments().getInt("recipe_id").toString(),
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

        val databaseClass = CleverKitchenDatabase(requireActivity())

        view.findViewById<TextView>(R.id.recipe_name).text = requireArguments().getString("recipe_name")
        view.findViewById<TextView>(R.id.description).text = requireArguments().getString("description")
        view.findViewById<TextView>(R.id.notes).text = requireArguments().getString("notes")
        view.findViewById<TextView>(R.id.recipe_sub_name).text = requireArguments().getString("recipe_name") + ": A classic Indian dish"
        view.findViewById<TextView>(R.id.how_to).text = "How to make " + requireArguments().getString("recipe_name")+"?"

        var imageUriValue = Uri.parse(requireArguments().getString("img_location"))

        Glide.with(requireContext())
            .load(requireArguments().getString("img_location")) // firebase url
            .into(view.findViewById<ImageView>(R.id.imageView_recipe_view));

        view.findViewById<ImageView>(R.id.imageView_recipe_view).setImageURI(imageUriValue)

        val recipe_id = requireArguments().getInt("recipe_id").toString()

        var is_fav = requireArguments().getInt("is_fav")
        updateFavoriteIcon(is_fav)

        view.findViewById<ImageView>(R.id.like_recipe).setOnClickListener {
            is_fav = if (is_fav == 1)  0 else 1

            updateFavoriteIcon(is_fav)
            databaseClass.toggleFavorite(recipe_id, is_fav)

            Toast.makeText(requireContext(), if (is_fav == 1) "Added to Favorites" else "Removed from Favorites", Toast.LENGTH_SHORT).show()
        }

        val chipGroup: ChipGroup = view.findViewById(R.id.chip_Group)

        val storedIngredientList = requireArguments().getString("ingredients")
        val storedIngredientListArray:List<String> = storedIngredientList!!.split(",")

        if(storedIngredientListArray.isNotEmpty())
            for(item in storedIngredientListArray.filter { item -> item.isNotEmpty() }){
                val chip = Chip(requireActivity())
                chip.text = item
                chipGroup.addView(chip)
            }



    }

    private fun updateFavoriteIcon(is_fav: Int) {
        if(is_fav === 1) {
            requireView().findViewById<ImageView>(R.id.like_recipe).setImageResource(R.drawable.heart)
        } else {
            requireView().findViewById<ImageView>(R.id.like_recipe).setImageResource(R.drawable.heart_outline)
        }
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