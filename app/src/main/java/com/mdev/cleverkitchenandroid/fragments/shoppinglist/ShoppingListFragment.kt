package com.mdev.cleverkitchenandroid.fragments.shoppinglist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase

class ShoppingListFragment : Fragment() {

    private lateinit var chipGroup: ChipGroup

    private fun addChip(shoppingListArray: Array<String>){
        for(item in shoppingListArray){
            Log.d("element",item)
            val chip = Chip(requireActivity())
            chip.text = item
            chip.isCloseIconVisible = true
            chip.setOnCloseIconClickListener{
                chipGroup.removeView(chip)
            }
            chipGroup.addView(chip)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        var editText = view.findViewById<TextView>(R.id.listInput)
        var addButton = view.findViewById<Button>(R.id.saveShoppingListBtn)
        chipGroup = view.findViewById(R.id.chipGroup)
        val database = CleverKitchenDatabase(requireActivity())
        val sharedPreferences =  activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val emailId = sharedPreferences?.getString("emailId","")
        val storedShoppingList = database.getShoppingList(emailId.toString());
        val storedShoppingListArray:Array<String> = storedShoppingList.split(",").toTypedArray()
        addChip(storedShoppingListArray)

        addButton.setOnClickListener{
            if(!editText.text.toString().isEmpty()){
                val shoppingList = editText.text.toString()
                 val shoppingListArray:Array<String> = shoppingList.split(",").toTypedArray()
                addChip(shoppingListArray)
                database.insertShoppingList(storedShoppingList+shoppingListArray.joinToString { it },emailId.toString())
                editText.setText("")
            }
        }
        return view
    }
}