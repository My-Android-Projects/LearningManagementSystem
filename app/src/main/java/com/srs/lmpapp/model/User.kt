package com.srs.lmpapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
 class User(
    var id: String = "",
    val type:String="",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val gender: String = "",

    val profileCompleted: Int = 0):Parcelable
// END