package com.mdev.cleverkitchenandroid.fragments.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.mdev.cleverkitchenandroid.model.ProfileViewModel
import com.mdev.cleverkitchenandroid.databinding.FragmentEditProfileBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import com.mdev.cleverkitchenandroid.model.User


class EditProfileBottomSheetFragment(email: String) : BottomSheetDialogFragment()
{
    private lateinit var binding: FragmentEditProfileBottomSheetBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        val database = CleverKitchenDatabase(requireActivity())
        val sharedPreferences =  activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val emailId = sharedPreferences?.getString("emailId","")
        profileViewModel = ViewModelProvider(activity).get(ProfileViewModel::class.java)
        val userDetails: User = database.getUser(emailId.toString());

        profileViewModel.firstName.observe(viewLifecycleOwner){
            binding.firstName.setText(userDetails.firstName)
        }

        profileViewModel.lastName.observe(viewLifecycleOwner){
            binding.lastName.setText(userDetails.lastName)
        }

        binding.saveButton.setOnClickListener {
            saveAction()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditProfileBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun saveAction() {
        val database = CleverKitchenDatabase(requireActivity())
        val sharedPreferences =  activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val emailId = sharedPreferences?.getString("emailId","")
        val firstName = binding.firstName.text.toString();
        val lastName = binding.lastName.text.toString();

        profileViewModel.firstName.value = firstName
        profileViewModel.lastName.value = lastName

        binding.firstName.setText(firstName)
        binding.lastName.setText(lastName)
        dismiss()
//        update paramss -------
        database.updateUser(emailId.toString(), firstName,lastName)
    }

}