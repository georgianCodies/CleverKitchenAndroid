package com.mdev.cleverkitchenandroid.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel: ViewModel() {
    var name = MutableLiveData<String>()
}