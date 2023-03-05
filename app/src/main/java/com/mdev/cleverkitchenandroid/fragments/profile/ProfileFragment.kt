package com.mdev.cleverkitchenandroid.fragments.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.mdev.cleverkitchenandroid.FirebaseStorageManager
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import com.mdev.cleverkitchenandroid.fragments.profile.EditProfileBottomSheetFragment
import com.mdev.cleverkitchenandroid.model.ProfileViewModel
import com.mdev.cleverkitchenandroid.model.User
import kotlinx.android.synthetic.main.fragment_edit_profile_bottom_sheet.view.*


@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {
    private lateinit var profileViewModel: ProfileViewModel
    private val pickImage = 100
    private var imageUri: Uri? = null
    private lateinit var profilePictureView: ImageView;
    private var emailId: String = "";
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false);
        val deleteAccountButton =  view.findViewById<Button>(R.id.deleteAccount)
        val logoutButton =  view.findViewById<Button>(R.id.logout)
        val editProfileButton = view.findViewById<Button>(R.id.editProfile)
        val database = CleverKitchenDatabase(requireActivity())
        val sharedPreferences =  activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        emailId = sharedPreferences?.getString("emailId","defaultName").toString()
        val editPicture = view.findViewById<ImageView>(R.id.edit_picture)
        val emailIdView = view.findViewById<TextView>(R.id.profile_emailId)
        val userNameView = view.findViewById<TextView>(R.id.profile_userName)

        val userDetails: User = database.getUser(emailId.toString())
        Log.d("userdetails",userDetails.toString())

        var imageUriValue = Uri.parse(userDetails.user_img_location)

        Glide.with(requireContext())
            .load(Uri.parse(userDetails.user_img_location)) // firebase url
            .into(view.findViewById(R.id.profilePicture));
        profilePictureView = view.findViewById(R.id.profilePicture)
        profilePictureView.setImageURI(imageUriValue)

        editPicture.setOnClickListener {
            println("upload image button clicked!")
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage,)
        }

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

        editProfileButton.setOnClickListener {
            if (emailId != null) {
                EditProfileBottomSheetFragment(emailId).show(
                    parentFragmentManager,
                    "editProfile"
                )
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            println(imageUri);
            if (imageUri !== null) {
                FirebaseStorageManager().uploadImage(
                    requireContext(),
                    "profile-images",
                    imageUri as Uri,
                    emailId
                ) { imageUri ->
                    Log.d("Update profile image - uploaded", imageUri.toString())
                    val database = CleverKitchenDatabase(requireActivity())
                    database.updateProfilePicture(emailId, imageUri.toString());
                    Glide.with(requireContext())
                        .load(Uri.parse(imageUri.toString())) // firebase url
                        .into(profilePictureView)
                }
            }

        }
    }

}