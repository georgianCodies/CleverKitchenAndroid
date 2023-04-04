package com.mdev.cleverkitchenandroid.fragments.viewrecipe

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filter.FilterListener
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import com.mdev.cleverkitchenandroid.model.Recipe
import com.mdev.cleverkitchenandroid.model.User


class ViewRecipeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_recipe, container, false)
        val databaseClass = CleverKitchenDatabase(requireActivity())

        val sharedPreferences = activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val emailId = sharedPreferences?.getString("emailId","")
        println("emailId:  " + emailId)

        val dataBaseArrayList:ArrayList<Recipe> =  databaseClass.getRecipeDetails(emailId.toString())
        println(dataBaseArrayList.toString())
        var userDetails:User =  databaseClass.getUser(emailId.toString())


        val rvRecipies: RecyclerView = view.findViewById(R.id.rv_recipies)
        rvRecipies.layoutManager = LinearLayoutManager(view.context)

        val receipesAdapter = FragmentViewRecipeAdapter(dataBaseArrayList, userDetails, databaseClass)
        rvRecipies.adapter = receipesAdapter

        val noFavRecipe = view.findViewById<TextView>(R.id.no_fav_recipes)

        val switch = view.findViewById<SwitchCompat>(R.id.material_switch)
        switch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                receipesAdapter.filter.filter("showFav") { i ->
                    if (i == 0) {
                        noFavRecipe.visibility = VISIBLE
                    } else {
                        noFavRecipe.visibility = INVISIBLE
                    }
                }
            } else {
                receipesAdapter.filter.filter("")
                noFavRecipe.visibility = INVISIBLE
            }
        }

        return view
    }
}


