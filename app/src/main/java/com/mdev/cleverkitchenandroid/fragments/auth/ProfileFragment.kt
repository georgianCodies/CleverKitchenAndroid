package com.mdev.cleverkitchenandroid.fragments.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import com.mdev.cleverkitchenandroid.model.User


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false);
        val deleteAccountButton =  view.findViewById<Button>(R.id.deleteAccount)
        val logoutButton =  view.findViewById<Button>(R.id.logout)
        val database = CleverKitchenDatabase(requireActivity())
        val sharedPreferences =  activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val emailId = sharedPreferences?.getString("emailId","")

        val emailIdView = view.findViewById<TextView>(R.id.profile_emailId)
        val userNameView = view.findViewById<TextView>(R.id.profile_userName)

        emailIdView.text = emailId

        if (emailId != null) {
           val userDetails: User =  database.getUser(emailId)
            userNameView.text = userDetails.name
        }
        deleteAccountButton.setOnClickListener {
            database.deleteUser(emailId.toString())
            Toast.makeText(this@ProfileFragment.requireActivity(), "Your account has been deleted", Toast.LENGTH_SHORT).show()
            view.findNavController().navigate(R.id.action_profileFragment_to_intialFragment)
        }
        logoutButton.setOnClickListener{
            val editor = sharedPreferences?.edit()
            editor?.clear()
            editor?.commit()
            Toast.makeText(this@ProfileFragment.requireActivity(), "Your are successfully logged out", Toast.LENGTH_SHORT).show()
            view.findNavController().navigate(R.id.action_profileFragment_to_intialFragment)
        }
        return view
    }

}