package com.mdev.cleverkitchenandroid

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController


class SignUpFragment : Fragment() {
    var name:String = ""
    var email:String = ""
    var password:String = ""
    var confirmpassword:String = ""
    var errorMessage:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        val nameTextView = view.findViewById<TextView>(R.id.inputNameSignUp)
        val emailTextView = view.findViewById<TextView>(R.id.inputEmailSignUp)
        val passwordTextView = view.findViewById<TextView>(R.id.inputPasswordSignUp)
        val confirmPasswordTextView = view.findViewById<TextView>(R.id.inputConfirmPasswordSignUp)
        val errorTextView = view.findViewById<TextView>(R.id.errorTextViewSignUp)
        val database = DatabaseClass(requireActivity())

        val signUpButton =  view.findViewById<Button>(R.id.signUpScreensignUpButton)
        signUpButton.setOnClickListener{
            errorTextView.text=""
            name = nameTextView.text.toString()
            email = emailTextView.text.toString()
            password = passwordTextView.text.toString()
            confirmpassword = confirmPasswordTextView.text.toString()
            if(validateFields()){
                if(database.CheckEmail(email)) {
                    database.Insert(email, name, password)
                    view.findNavController().popBackStack()
                }
                else{
                    errorTextView.text = "Emailid already exists"
                }
            }else{
                errorTextView.text = errorMessage
            }

        }
        val signInTextview =  view.findViewById<TextView>(R.id.signInInSignUpTextView)
        signInTextview.setOnClickListener{
            errorTextView.text=""
            view.findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)

        }
        return view
    }

    fun validateFields(): Boolean {

        if (name == "") {
            errorMessage = "Please enter the name"
            return false
        } else if (email == "") {
            errorMessage = "Please enter the email"
            return false
        } else if (password == "") {
            errorMessage = "Please enter the password"
            return false
        } else if (confirmpassword == "") {
            errorMessage = "Please confirm the password"
            return false
        } else if (password != confirmpassword) {
            errorMessage = "Password and Confirm password doesnot match"
            return false
        } else {
            return true
        }
    }






}