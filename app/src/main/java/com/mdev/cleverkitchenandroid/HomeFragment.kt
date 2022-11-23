package com.mdev.cleverkitchenandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_home, container, false);

        val viewRecipeButton =  view.findViewById<Button>(R.id.viewRecipes)

        viewRecipeButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_homeFragment_to_viewRecipeFragment)
        }
        val addRecipeButton =  view.findViewById<Button>(R.id.addRecipes)
        addRecipeButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_homeFragment_to_addRecipeFragment)
        }
        return view
    }


}