package com.srs.lmpapp.utils


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel  : ViewModel() {
    val appliedFilter = MutableLiveData<Map<String,Any>>()

    fun applyFilter( filter:Map<String,Any>) {
        appliedFilter.value=filter
    }
}