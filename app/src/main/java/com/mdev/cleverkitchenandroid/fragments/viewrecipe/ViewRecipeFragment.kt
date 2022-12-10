package com.mdev.cleverkitchenandroid.fragments.viewrecipe

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val sharedPreferences =  activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val emailId = sharedPreferences?.getString("emailId","")
        println("emailId:  "+emailId)
        val dataBaseArrayList:ArrayList<Recipe> =  databaseClass.getRecipeDetails(emailId.toString())
        println(dataBaseArrayList.toString())
        var userDetails:User =  databaseClass.getUser(emailId.toString())


        val rvRecipies: RecyclerView = view.findViewById(R.id.rv_recipies)
//        rvRecipies.setHasFixedSize(true);
        rvRecipies.layoutManager = LinearLayoutManager(view.context)
        val receipiesAdapter = FragmentViewRecipeAdapter(dataBaseArrayList,userDetails)
        rvRecipies.adapter =receipiesAdapter

        return view
    }



}


