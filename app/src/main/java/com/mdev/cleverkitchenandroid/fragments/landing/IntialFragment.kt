package com.mdev.cleverkitchenandroid.fragments.landing

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.NotificationCompat
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
        allowNotification();
        signInButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_intialFragment_to_SignInFragment)

        }
        signUpButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_intialFragment_to_signUpFragment)
        }
        return view
    }

    fun allowNotification() {
        val NOTIFICATION_CHANNEL_ID = "my_channel_id_01"
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "My Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "Channel description"
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}