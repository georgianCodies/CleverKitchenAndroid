package com.mdev.cleverkitchenandroid.fragments.recipedetails

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mdev.cleverkitchenandroid.R

class RecipeDetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_details, container, false)
    }
    //Set recipe here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.recipe_name).text = requireArguments().getString("recipe_name")
        view.findViewById<TextView>(R.id.description).text = requireArguments().getString("description")
        view.findViewById<TextView>(R.id.recipe_sub_name).text = requireArguments().getString("recipe_name") + ": A classic Indian dish"
        view.findViewById<TextView>(R.id.chip).text = requireArguments().getString("chip")
        view.findViewById<TextView>(R.id.how_to).text = "How to make " + requireArguments().getString("recipe_name")+"?"
        var imageUriValue = Uri.parse(requireArguments().getString("img_location"))
        view.findViewById<ImageView>(R.id.imageView_recipe_view).setImageURI(imageUriValue)
    }
}