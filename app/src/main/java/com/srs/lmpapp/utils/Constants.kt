package com.srs.lmpapp.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val TBL_USERS: String = "users"
    const val TBL_COURSES: String = "courses"
    const val TBL_ENROLLED_COURSES: String = "enrolled_courses"
    const val MYLMSAPP_PREFERENCES: String = "MyLMSAppPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val LOGGED_IN_USER_EMAIL: String= "logged_in_user_email"
    const val LOGGED_IN_USERID:String="logged_in_userid"
    const val  LOGGED_IN_USER_PHONE:String="logged_in_user+phone"
    const val LOGGED_IN_USERTYPE:String="logged_in_usertype"
    const val LOGGED_IN_USER_CREDITS:String="logged_in_user+credit"
    const val LOGGED_IN_USER_COURSES:String="logged_in_user_coursess"
    const val CURRENT_ENROLLED_COURSE_ID:String="current_course_id"

    const val EXTRA_USER_DETAILS:String="extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
 const val TOT_CREDITS:String="totcredits"
    const val TOT_COURSES:String="totcourses"
    const val MODULES="modules"

    const val PICK_IMAGE_REQUEST_CODE = 2
    const val CURRENT_COURSE:String="currentCourse"
    const val CURRENT_COURSE_ID:String="currentCourseId"

    const val TAKEN_BY: String = "takenby"
    const val ENROLLED_BY: String = "enrolledby"

    // Constant variables for Gender
    const val MALE: String = "Male"
    const val FEMALE: String = "Female"

    // Firebase database field names
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val IMAGE: String = "image"
    const val COURSE_IMAGE: String = "Course_Iamge"
    // TODO Step 1: Add the constant database field for complete profile.
    // START
    const val COMPLETE_PROFILE: String = "profileCompleted"
    // END

    const val USER_PROFILE_IMAGE:String = "User_Profile_Image"
    const val FILTER_COURSE_CREDITS:String="credits"
    const val FILTER_COURSE_CATEGORY:String ="category"

    /**
     * A function for user profile image selection from phone storage.
     */
    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    /**
     * A function to get the image file extension of the selected image.
     *
     * @param activity Activity reference.
     * @param uri Image file uri.
     */
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}