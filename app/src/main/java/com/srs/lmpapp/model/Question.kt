package com.srs.lmpapp.model


import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
class Question (
    var id:String?="",
    var question:String?="",
    var multiplechoice:Boolean?=false,
    var marks:Int?=0,
    var questionoptions:List<String>?=ArrayList<String>(),
    var answeroptions:List<String>?=ArrayList<String>(),

):Parcelable