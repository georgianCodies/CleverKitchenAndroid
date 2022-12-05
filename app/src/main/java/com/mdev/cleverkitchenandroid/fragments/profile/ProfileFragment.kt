package com.mdev.cleverkitchenandroid.fragments.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.mdev.cleverkitchenandroid.model.ProfileViewModel
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import com.mdev.cleverkitchenandroid.model.User
import com.mdev.cleverkitchenandroid.databinding.ActivityMainBinding


class ProfileFragment : Fragment() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false);
        val deleteAccountButton =  view.findViewById<Button>(R.id.deleteAccount)
        val logoutButton =  view.findViewById<Button>(R.id.logout)
        val editProfileButton =  view.findViewById<Button>(R.id.modalBottomSheetButton)
        val database = CleverKitchenDatabase(requireActivity())
        val sharedPreferences =  activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val emailId = sharedPreferences?.getString("emailId","")

        val emailIdView = view.findViewById<TextView>(R.id.profile_emailId)
        val userNameView = view.findViewById<TextView>(R.id.profile_userName)

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)

        emailIdView.text = emailId
        if (emailId != null) {
            val userDetails: User = database.getUser(emailId)
            Log.d("username",userDetails.name)
            userNameView.text = userDetails.name
        }

        profileViewModel.name.observe(viewLifecycleOwner) {
            if (it.toString().isNotEmpty()) {
                userNameView.text = it.toString()
            } else {
                if (emailId != null) {
                    val userDetails: User = database.getUser(emailId)
                    Log.d("username",userDetails.name)
                    userNameView.text = userDetails.name
                }
            }
        }
        profileViewModel.name.value = userNameView.text.toString()

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

        editProfileButton.setOnClickListener {
            EditProfileBottomSheetFragment(userNameView.text).show(parentFragmentManager, "editProfile")
        }

        return view
    }

}