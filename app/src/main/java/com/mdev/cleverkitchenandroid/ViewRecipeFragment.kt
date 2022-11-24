package com.mdev.cleverkitchenandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ViewRecipeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_recipe, container, false)

        val arrayList= ArrayList<RecipiesModel>()
        arrayList.add(RecipiesModel("Indian Creamy butter chicken curry recipe","#hasttag1, #hashtag2", R.drawable.ic_dish))
        arrayList.add(RecipiesModel("Italian tomato pasta recipe","#hasttag1, #hashtag2", R.drawable.ic_dish2))

        val rvRecipies: RecyclerView = view.findViewById(R.id.rv_recipies)
        //rvRecipies.setHasFixedSize(true);
        rvRecipies.layoutManager = LinearLayoutManager(view.context);
        val receipiesAdapter = FragmentViewRecipeAdapter(arrayList)
        rvRecipies.adapter =receipiesAdapter

        return view
    }



}