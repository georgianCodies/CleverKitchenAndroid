package com.mdev.cleverkitchenandroid.fragments.viewrecipe

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.model.Recipe
import com.mdev.cleverkitchenandroid.model.User
import java.text.SimpleDateFormat
import java.util.*

class FragmentViewRecipeAdapter(private val recipiesList: List<Recipe>,private  val profileDetails : User) :
    RecyclerView.Adapter<FragmentViewRecipeAdapter.ViewHolder>() {
    var ctx :Context? = null
    // create new views

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        ctx = parent.context

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }



    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipiesModelList = recipiesList[position]
        // sets the image to the imageview from our itemHolder class
        // sets the text to the textview from our itemHolder class
        Log.d("Image", recipiesModelList.toString())
        holder.tvDesc.text = recipiesModelList.description
//        holder.ivDish.setImageResource(recipiesModelList.img_location)
        holder.tvTag.text = recipiesModelList.ingredients
        Glide.with(ctx!!).load(recipiesModelList.img_location).into(holder.ivDish)
        holder.tvDate.text = getCurrentDate()
        holder.tvName.text = profileDetails.name

        holder.itemView.setOnClickListener{
                holder.itemView.findNavController().navigate(R.id.action_viewRecipeFragment_to_recipeDetailsFragment, Bundle().apply {
                    putString("recipe_id", recipiesModelList.recipe_id.toString())
                    putString("recipe_name", recipiesModelList.recipe_name)
                    putString("chip", recipiesModelList.ingredients)
                    putString("description", recipiesModelList.description)
                    putString("notes", recipiesModelList.notes)
                    putString("img_location", recipiesModelList.img_location)
//                    putString("email_id", recipiesModelList.email_id)
                })
            }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return recipiesList.size
    }

    fun getCurrentDate():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(Date())
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ivDish: ImageView = itemView.findViewById(R.id.iv_dish)
        val tvDesc: TextView = itemView.findViewById(R.id.tv_desc)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvTag: TextView = itemView.findViewById(R.id.tv_tag)
        val tvName:TextView = itemView.findViewById(R.id.tv_name)

    }
}


