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
    private var storedShoppingList: String = "";
    private lateinit var finalShoppingList: List<String> ;

    private fun addChip(shoppingListArray: List<String>){
        Log.d("addchip",shoppingListArray.toString())

        for(item in shoppingListArray){
            Log.d("element",item)
            val chip = Chip(requireActivity())
            chip.text = item
            chip.isCloseIconVisible = true
            chip.setOnCloseIconClickListener{
                val database = CleverKitchenDatabase(requireActivity())
                val sharedPreferences =  activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
                val emailId = sharedPreferences?.getString("emailId","")
                val updatedShoppingList = finalShoppingList.filter { item -> item != chip.text.toString() }

                chipGroup.removeView(chip)
                finalShoppingList = updatedShoppingList

                Log.d("final array",finalShoppingList.joinToString { it })
                database.insertShoppingList(finalShoppingList.joinToString { it }, emailId)
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
        val database = CleverKitchenDatabase(requireActivity())
        val sharedPreferences =  activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val emailId = sharedPreferences?.getString("emailId","")

        chipGroup = view.findViewById(R.id.chipGroup)

        storedShoppingList = database.getShoppingList(emailId.toString());
        val storedShoppingListArray:List<String> = storedShoppingList.split(",")

        Log.d("Stored",storedShoppingList)

        if(storedShoppingList.isNotEmpty())
        addChip(storedShoppingListArray.filter { item -> item.isNotEmpty() })

        finalShoppingList = storedShoppingListArray.filter { item -> item.isNotEmpty() }

        addButton.setOnClickListener{
            if(editText.text.toString().isNotEmpty()){
                val shoppingList = editText.text.toString()
                val shoppingListArray:List<String> = shoppingList.split(",")
                addChip(shoppingListArray.filter { item -> item.isNotEmpty() })
                finalShoppingList+=shoppingListArray;
                Log.d("final Array",finalShoppingList.joinToString())
                database.insertShoppingList(finalShoppingList.joinToString { it },emailId)
                editText.text = ""
            }
        }
        return view
    }
}