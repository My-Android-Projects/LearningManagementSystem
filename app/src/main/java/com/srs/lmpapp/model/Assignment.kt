package com.srs.lmpapp.model


import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
class Assignment (
    var id:String?="",
    var name:String?="",
    var description:String?="",
    var enddate:String?="",
    var submissioncomments:String?="",
    var status:String?="",
    var referencedocuments: List<String> ?= ArrayList<String>(),
    var submissiondocuments: List<String> ?= ArrayList<String>()
):Parcelable