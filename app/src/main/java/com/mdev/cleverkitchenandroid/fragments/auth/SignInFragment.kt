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


class SignInFragment : Fragment() {
    var email:String = ""
    var password:String = ""
    var errorMessage:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        val emailTextView = view.findViewById<TextView>(R.id.inputEmailSignIn)
        val passwordTextView = view.findViewById<TextView>(R.id.inputPasswordSignIn)
        val signInButton =  view.findViewById<Button>(R.id.signInScreenSignInButton)
        val database = CleverKitchenDatabase(requireActivity())

        signInButton.setOnClickListener{
            email = emailTextView.text.toString()
            password = passwordTextView.text.toString()
            if(validateFields()){
                if(database.checkLogin(email,password)){
                    val sharedPreference =  activity?.getSharedPreferences("userDetails",Context.MODE_PRIVATE)
                    var editor = sharedPreference?.edit()
                    editor?.putString("emailId",email)
                    editor?.commit()
                    view.findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                }
                else{
                    Toast.makeText(this@SignInFragment.requireActivity(), "Please sign up before your login", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@SignInFragment.requireActivity(), "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
        val signUpTextView =  view.findViewById<TextView>(R.id.signUpInSignInTextView)
        signUpTextView.setOnClickListener{
            view.findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)

        }
        return view
    }

    fun validateFields(): Boolean {

      if (email == "" && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
          Toast.makeText(this@SignInFragment.requireActivity(), "Please enter valid email", Toast.LENGTH_SHORT).show()
          return false
        } else if (password == "") {
          Toast.makeText(this@SignInFragment.requireActivity(), "Please enter your password", Toast.LENGTH_SHORT).show()
          return false
        }
        return true
    }
}