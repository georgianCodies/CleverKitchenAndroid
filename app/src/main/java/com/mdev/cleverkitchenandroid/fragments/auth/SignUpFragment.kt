package com.mdev.cleverkitchenandroid.fragments.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mdev.cleverkitchenandroid.R
import com.mdev.cleverkitchenandroid.database.CleverKitchenDatabase
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class SignUpFragment : Fragment() {
    var name:String = ""
    var email:String = ""
    var password:String = ""
    var confirmPassword:String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        val nameTextView = view.findViewById<TextView>(R.id.inputNameSignUp)
        val emailTextView = view.findViewById<TextView>(R.id.inputEmailSignUp)
        val passwordTextView = view.findViewById<TextView>(R.id.inputPasswordSignUp)
        val confirmPasswordTextView = view.findViewById<TextView>(R.id.inputConfirmPasswordSignUp)
        val database = CleverKitchenDatabase(requireActivity())

        val signUpButton =  view.findViewById<Button>(R.id.signUpScreenSignUpButton)
        signUpButton.setOnClickListener{
            name = nameTextView.text.toString()
            email = emailTextView.text.toString()
            password = passwordTextView.text.toString()
            confirmPassword = confirmPasswordTextView.text.toString()
            if(validateFields()){
                if(database.checkEmail(email)) {
                    database.insertUser(email, name, password)
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

    private fun validateFields(): Boolean {

        if(name.isEmpty()){
            Toast.makeText(this@SignUpFragment.requireActivity(), "Please enter your name", Toast.LENGTH_SHORT).show()
            return false
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(
                this@SignUpFragment.requireActivity(), "Please enter valid email id", Toast.LENGTH_SHORT).show()
            return false
        } else if(password.isEmpty()){
            Toast.makeText(this@SignUpFragment.requireActivity(), "Please enter your password", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
            Toast.makeText(this@SignUpFragment.requireActivity(), "Passwords doesn't match", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


}
