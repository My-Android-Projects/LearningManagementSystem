
package com.srs.lmpapp.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
class Topic (
    var id:String?="",
    var name:String?="",
    var topicvideo:String?="",
): Parcelable