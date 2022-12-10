package com.mdev.cleverkitchenandroid.fragments.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import com.mdev.cleverkitchenandroid.model.Recipe


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false);

        val viewRecipeButton =  view.findViewById<Button>(R.id.viewRecipes)
        val addRecipeButton =  view.findViewById<Button>(R.id.addRecipes)
        val shoppingListButton =  view.findViewById<Button>(R.id.shoppingList)

        viewRecipeButton.setOnClickListener{
            val databaseClass = CleverKitchenDatabase(requireActivity())
            val sharedPreferences =  activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
            val emailId = sharedPreferences?.getString("emailId","")
            val recipeList:ArrayList<Recipe> =  databaseClass.getRecipeDetails(emailId.toString())
            if(recipeList.isNotEmpty()) {
                view.findNavController().navigate(R.id.action_homeFragment_to_viewRecipeFragment)
            }else{
                Toast.makeText(this@HomeFragment.requireActivity(), "No recipes found", Toast.LENGTH_SHORT).show()
            }
        }
        addRecipeButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_homeFragment_to_addRecipeFragment)
        }
        shoppingListButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_homeFragment_to_shoppingListFragment)
        }

        return view
    }


}