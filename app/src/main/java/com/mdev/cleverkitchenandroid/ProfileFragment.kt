package com.mdev.cleverkitchenandroid

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
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import com.mdev.cleverkitchenandroid.model.ProfileViewModel
import com.mdev.cleverkitchenandroid.model.User
import kotlinx.android.synthetic.main.fragment_edit_profile_bottom_sheet.view.*


class ProfileFragment : Fragment() {
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false);
        val deleteAccountButton =  view.findViewById<Button>(R.id.deleteAccount)
        val logoutButton =  view.findViewById<Button>(R.id.logout)
        val database = CleverKitchenDatabase(requireActivity())
        val sharedPreferences =  activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val emailId = sharedPreferences?.getString("emailId","defaultName")

        val emailIdView = view.findViewById<TextView>(R.id.profile_emailId)
        val userNameView = view.findViewById<TextView>(R.id.profile_userName)
        val userDetails: User = database.getUser(emailId.toString())
        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)

        emailIdView.text = emailId


        profileViewModel.firstName.observe(viewLifecycleOwner) {

            Log.d("-------fname",it.toString())
            if (it.toString().isNotEmpty()) {
                userNameView.text = it.toString()
            } else {
                if (emailId != null) {
                    val userDetails: User = database.getUser(emailId)
                    Log.d("username",userDetails.firstName+" "+ userDetails.lastName)
                    userNameView.text = userDetails.firstName
                }
            }
        }
        profileViewModel.firstName.value = userDetails.firstName

        profileViewModel.lastName.observe(viewLifecycleOwner) {
            Log.d("-------lname",it.toString())
            if (it.toString().isNotEmpty()) {
                userNameView.text =  userNameView.text.toString() +" "+ it.toString()
            } else {
                if (emailId != null) {
                    val userDetails: User = database.getUser(emailId)
                    Log.d("username",userDetails.firstName+" "+ userDetails.lastName)
                    userNameView.text = userNameView.text.toString() +" "+ userDetails.lastName
                }
            }
        }
        profileViewModel.lastName.value = userDetails.lastName

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

//        editProfileButton.setOnClickListener {
//            if (emailId != null) {
//                EditProfileBottomSheetFragment(emailId).show(
//                    parentFragmentManager,
//                    "editProfile"
//                )
//            }
//        }

        return view
    }

}