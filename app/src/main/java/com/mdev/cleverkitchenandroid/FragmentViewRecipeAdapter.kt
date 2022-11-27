package com.mdev.cleverkitchenandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mdev.cleverkitchenandroid.model.Recipe

class FragmentViewRecipeAdapter(private val recipiesList: List<Recipe>) :
    RecyclerView.Adapter<FragmentViewRecipeAdapter.ViewHolder>() {
    // create new views

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
//        view.setOnClickListener{
//            val bundle = bundleOf("SOME_BUNDLE_KEY" to recipiesList)
//            view.findNavController().navigate(R.id.action_viewRecipeFragment_to_recipeDetailsFragment)
//        }
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipiesModelList = recipiesList[position]
        // sets the image to the imageview from our itemHolder class
        // sets the text to the textview from our itemHolder class
//        holder.
        holder.tvDesc.text = recipiesModelList.description
        holder.ivDish.setImageResource(R.drawable.ic_dish2)
        holder.tvTag.text = recipiesModelList.ingredients
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return recipiesList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ivDish: ImageView = itemView.findViewById(R.id.iv_dish)
        val tvDesc: TextView = itemView.findViewById(R.id.tv_desc)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvTag: TextView = itemView.findViewById(R.id.tv_tag)

    }
}

