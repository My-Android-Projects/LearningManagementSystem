package com.srs.lmpapp.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.srs.lmpapp.R
import com.srs.lmpapp.utils.Constants
import kotlinx.android.synthetic.main.activity_faculty_home.*

class FacultyHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_faculty_home)

        val sharedPreferences =
            getSharedPreferences(Constants.MYLMSAPP_PREFERENCES, Context.MODE_PRIVATE)

        val userId = sharedPreferences.getString(Constants.LOGGED_IN_USERID,"")
        val emailId = sharedPreferences.getString(Constants.LOGGED_IN_EMAIL,"")
        val userType= sharedPreferences.getString(Constants.LOGGED_IN_USERTYPE,"")
        val userName= sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,"")

        tv_user_id.text = "User ID :: $userId"
        tv_email_id.text = "Email ID :: $emailId"
        tv_userType.text="User Type:: $userType"
        tv_user_name.text="User Name: $userName"

        btn_logout.setOnClickListener {
            // Logout from app.
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this@FacultyHomeActivity, LoginActivity::class.java))
            finish()
        }
        // END
    }
}