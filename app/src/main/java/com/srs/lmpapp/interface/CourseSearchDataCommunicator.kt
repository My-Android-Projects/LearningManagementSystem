package com.srs.lmpapp.`interface`

interface CourseSearchDataCommunicator {
    fun sendCourseFilterData(courseName:String,courseCategory:String,courseCredits:String,
                             courseStartDate:String,courseEndDate:String)
}