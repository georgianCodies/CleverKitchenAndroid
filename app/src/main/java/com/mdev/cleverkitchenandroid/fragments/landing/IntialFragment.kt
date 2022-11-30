package com.mdev.cleverkitchenandroid.fragments.landing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.mdev.cleverkitchenandroid.R


class IntialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_intial, container, false);
        val signInButton =  view.findViewById<Button>(R.id.intialSignIn)
        val signUpButton =  view.findViewById<Button>(R.id.intialSignUp)

        signInButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_intialFragment_to_SignInFragment)

        }
        signUpButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_intialFragment_to_signUpFragment)
        }
        return view
    }


}