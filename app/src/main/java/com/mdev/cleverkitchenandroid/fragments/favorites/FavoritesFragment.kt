package com.mdev.cleverkitchenandroid.fragments.favorites

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import com.mdev.cleverkitchenandroid.model.Recipe
import com.mdev.cleverkitchenandroid.model.User

class FavoritesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        val databaseClass = CleverKitchenDatabase(requireActivity())

        val sharedPreferences =  activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val emailId = sharedPreferences?.getString("emailId", "")
        val dataBaseArrayList:ArrayList<Recipe> = databaseClass.getRecipeDetails(emailId.toString(), "1")
        val userDetails: User = databaseClass.getUser(emailId.toString())

        if(dataBaseArrayList.size > 0) {
            view.findViewById<ScrollView>(R.id.scroll_list).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.no_favorites).visibility = View.GONE
        } else {
            view.findViewById<ScrollView>(R.id.scroll_list).visibility = View.GONE
            view.findViewById<TextView>(R.id.no_favorites).visibility = View.VISIBLE
        }

        val rvRecipies: RecyclerView = view.findViewById(R.id.favorite_recipes)
        rvRecipies.layoutManager = LinearLayoutManager(view.context)

        val receipiesAdapter = FavoriteRecipeAdapter(dataBaseArrayList, userDetails)
        rvRecipies.adapter = receipiesAdapter

        return view
    }
}