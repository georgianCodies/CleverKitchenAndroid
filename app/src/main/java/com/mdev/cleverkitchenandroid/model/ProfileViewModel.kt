package com.mdev.cleverkitchenandroid.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel: ViewModel() {
    var firstName = MutableLiveData<String>()
    var lastName = MutableLiveData<String>()
    var userName = MutableLiveData<String>()
    var mobileNumber = MutableLiveData<String>()
}