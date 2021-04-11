package com.srs.lmpapp.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class MyObseravble : ViewModel()
{
    val data = MutableLiveData<String>()

    fun data(item1: String) {
        data.value = item1

    }
}