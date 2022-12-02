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


class EditProfileBottomSheetFragment(text: CharSequence) : BottomSheetDialogFragment()
{
    private lateinit var binding: FragmentEditProfileBottomSheetBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        profileViewModel = ViewModelProvider(activity).get(ProfileViewModel::class.java)

        profileViewModel.name.observe(viewLifecycleOwner){
            binding.inputName.setText(it.toString())
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
        val userName = binding.inputName.text.toString();
        profileViewModel.name.value = userName
        binding.inputName.setText(userName)
        dismiss()
        database.updateUser(emailId.toString(), userName)
    }

}