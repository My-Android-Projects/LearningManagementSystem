package com.srs.lmpapp.model

//TODO Step 1: Create a data model class for User with the required fields.
// Here we have added all the fields of User which is present in the registration screen and which will be used later on.

// START
/**
 * A data model class for User with required fields.
 */
// START
data class User(
    val id: String = "",
    val type:String="",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val gender: String = "",
    val profileCompleted: Int = 0)
// END