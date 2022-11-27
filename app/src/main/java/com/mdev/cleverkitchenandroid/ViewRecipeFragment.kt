package com.mdev.cleverkitchenandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import com.mdev.cleverkitchenandroid.model.Recipe


class ViewRecipeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_recipe, container, false)
        val databaseClass = CleverKitchenDatabase(requireActivity())
        val dataBaseArrayList:ArrayList<Recipe> =  databaseClass.getRecipeDetails("ch")
        println(dataBaseArrayList.toString())
        val arrayList = ArrayList<Recipe>()
        arrayList.add(Recipe(1,"Indian Creamy butter chicken curry recipe","#hasttag1, #hashtag2", "R.drawable.ic_dish","R","ch"))
        arrayList.add(Recipe(2,"Italian tomato pasta recipe","#hasttag1, #hashtag2", "R.drawable.ic_dish2","R","ch"))



        val rvRecipies: RecyclerView = view.findViewById(R.id.rv_recipies)
//        rvRecipies.setHasFixedSize(true);
        rvRecipies.layoutManager = LinearLayoutManager(view.context)
        val receipiesAdapter = FragmentViewRecipeAdapter(arrayList)
        rvRecipies.adapter =receipiesAdapter

        return view
    }



}


