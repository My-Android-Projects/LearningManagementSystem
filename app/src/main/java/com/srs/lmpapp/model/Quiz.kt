package com.srs.lmpapp.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
class Quiz (

    var id:String?="",
    var coourseid:String?="",
    var moduleid:String?="",
    var  questions:List<String>?=ArrayList<String>()

        ): Parcelable
