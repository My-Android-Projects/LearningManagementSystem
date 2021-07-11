package com.srs.lmpapp.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
class Module (
    var id:String?="",
    var name:String?="",
    var duration:Int?=0,
    var status:String?="",
    var topics:List<String>?=ArrayList<String>()
): Parcelable