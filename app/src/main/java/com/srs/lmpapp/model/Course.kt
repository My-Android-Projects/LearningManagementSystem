package com.srs.lmpapp.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
 class Course(
        val name:String?="",
        val category:String?="",
        val credits: Long=0,
        val takenby:String?="",
        val totseats: Long=0,
        var remainingseats:Long=0,
        val startdate: String?="",
        val enddate:String?="",
        val courseimage:String?="",
        var id:String=""
    ): Parcelable
