package com.mdev.cleverkitchenandroid.fragments.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mdev.cleverkitchenandroid.FirebaseStorageManager
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class SignUpFragment : Fragment() {
    var name:String = ""
    var fname:String = ""
    var lname:String = ""
    var mobileno:String = ""
    var email:String = ""
    var password:String = ""
    var confirmPassword:String = ""
    private var mStorageRef: StorageReference? = null
    private val pickImage = 100
    private var imageUri: Uri? = null
    var filePath: ValueCallback<Array<Uri>>? = null
    private lateinit var imageButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        val firstNameTextView = view.findViewById<TextView>(R.id.inputFNameSignUp)
        val lastNameTextView = view.findViewById<TextView>(R.id.inputLNameSignUp)
        val mobileTextView = view.findViewById<TextView>(R.id.inputMNOSignUp)
        val nameTextView = view.findViewById<TextView>(R.id.inputNameSignUp)
        val emailTextView = view.findViewById<TextView>(R.id.inputEmailSignUp)
        val passwordTextView = view.findViewById<TextView>(R.id.inputPasswordSignUp)
        val confirmPasswordTextView = view.findViewById<TextView>(R.id.inputConfirmPasswordSignUp)
        val database = CleverKitchenDatabase(requireActivity())
        mStorageRef = FirebaseStorage.getInstance().getReference()

        var isAllFieldsChecked = false;
        var imgURI = Uri.parse("");
        imageButton =  view.findViewById<Button>(R.id.signUpScreenImageButton)
        imageButton.setOnClickListener {
            println("upload image button clicked!")
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        val signUpButton =  view.findViewById<Button>(R.id.signUpScreenSignUpButton)
        signUpButton.setOnClickListener{
            name = nameTextView.text.toString()
            fname = firstNameTextView.text.toString()
            lname = lastNameTextView.text.toString()
            mobileno = mobileTextView.text.toString()
            email = emailTextView.text.toString()
            password = passwordTextView.text.toString()
            confirmPassword = confirmPasswordTextView.text.toString()

            if(validateFields()){
                if(database.checkEmail(email)) {
                    imgURI = imageButton.tag as Uri?
                    if(imgURI !== null) {
                        FirebaseStorageManager().uploadImage(
                            requireContext(),
                            "profile-images",
                            imgURI,
                            email
                        ) { imageUri ->
                            Log.d("Add profile image- uploaded", imageUri.toString())
                            database.insertUser(
                                email,
                                fname,
                                lname,
                                mobileno,
                                imageUri.toString(),
                                name,
                                password
                            )
                        }
                    }else{
                        Log.d("Add profile image - default ", imageUri.toString())
                        database.insertUser(
                            email,
                            fname,
                            lname,
                            mobileno,
                            "",
                            name,
                            password
                        )
                    }
                    view.findNavController().popBackStack()
                }
                else{
                    Toast.makeText(this@SignUpFragment.requireActivity(), "Email Id already exists", Toast.LENGTH_SHORT).show()
                }

            }
        }
        val signInTextview =  view.findViewById<TextView>(R.id.signInInSignUpTextView)
        signInTextview.setOnClickListener{
            view.findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            println(imageUri);
            Log.d("image uri",imageUri.toString())
           imageButton.setTag(imageUri)
            //imageView.setImageURI(imageUri)
        }


    }

    private fun validateFields(): Boolean {

        if(fname.isEmpty()){
            Toast.makeText(this@SignUpFragment.requireActivity(), "Please enter your first name", Toast.LENGTH_SHORT).show()
            return false
        }else if(lname.isEmpty()){
            Toast.makeText(this@SignUpFragment.requireActivity(), "Please enter your last name", Toast.LENGTH_SHORT).show()
            return false
        }else if(mobileno.isEmpty()){
            Toast.makeText(this@SignUpFragment.requireActivity(), "Please enter your mobile number", Toast.LENGTH_SHORT).show()
            return false
        }else if(name.isEmpty()){
            Toast.makeText(this@SignUpFragment.requireActivity(), "Please enter your user name", Toast.LENGTH_SHORT).show()
            return false
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(
                this@SignUpFragment.requireActivity(), "Please enter valid email id", Toast.LENGTH_SHORT).show()
            return false
        } else if(password.isEmpty()){
            Toast.makeText(this@SignUpFragment.requireActivity(), "Please enter your password", Toast.LENGTH_SHORT).show()
            return false
        }else if(confirmPassword.isEmpty()){
            Toast.makeText(this@SignUpFragment.requireActivity(), "Please confirm your password", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
            Toast.makeText(this@SignUpFragment.requireActivity(), "Passwords doesn't match", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


}
