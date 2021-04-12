package com.srs.lmpapp.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
 class Course(
       var name:String?="",
        var category:String?="",
        var credits: Long=0,
        var takenby:String?="",
        var totseats: Long=0,
        var remainingseats:Long=0,
        var startdate: String?="",
        var enddate:String?="",
        var courseimage:String?="",
        var id:String="",
        var enrolledby:List<String>?=ArrayList<String>(),
        var description:String?="",
        var modules:List<String>?=ArrayList<String>()
    ): Parcelable
