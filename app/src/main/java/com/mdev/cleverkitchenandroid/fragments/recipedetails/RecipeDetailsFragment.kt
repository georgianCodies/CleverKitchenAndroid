package com.mdev.cleverkitchenandroid.fragments.recipedetails

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase

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

        val databaseClass = CleverKitchenDatabase(requireActivity())

        view.findViewById<TextView>(R.id.recipe_name).text = requireArguments().getString("recipe_name")
        view.findViewById<TextView>(R.id.description).text = requireArguments().getString("description")
        view.findViewById<TextView>(R.id.recipe_sub_name).text = requireArguments().getString("recipe_name") + ": A classic Indian dish"
        view.findViewById<TextView>(R.id.chip).text = requireArguments().getString("chip")
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
    }

    private fun updateFavoriteIcon(is_fav: Int) {
        if(is_fav === 1) {
            requireView().findViewById<ImageView>(R.id.like_recipe).setImageResource(R.drawable.heart)
        } else {
            requireView().findViewById<ImageView>(R.id.like_recipe).setImageResource(R.drawable.heart_outline)
        }
    }

}