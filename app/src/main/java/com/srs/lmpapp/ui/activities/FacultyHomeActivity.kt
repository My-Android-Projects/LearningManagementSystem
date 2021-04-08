package com.srs.lmpapp.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityFacultyHomeBinding
import com.srs.lmpapp.utils.Constants
//import kotlinx.android.synthetic.main.activity_faculty_home.*

class FacultyHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFacultyHomeBinding

        override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

            binding=ActivityFacultyHomeBinding.inflate(layoutInflater)
            setContentView(binding.root)


        val sharedPreferences =
            getSharedPreferences(Constants.MYLMSAPP_PREFERENCES, Context.MODE_PRIVATE)

        val userId = sharedPreferences.getString(Constants.LOGGED_IN_USERID,"")
        val emailId = sharedPreferences.getString(Constants.LOGGED_IN_EMAIL,"")
        val userType= sharedPreferences.getString(Constants.LOGGED_IN_USERTYPE,"")
        val userName= sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,"")
            binding.tvUserId.text = "User ID :: $userId"

            binding.tvEmailId.text = "Email ID :: $emailId"
            binding.tvUserType.text="User Type:: $userType"
            binding.tvUserName.text="User Name: $userName"
            binding.btnLogout.setOnClickListener {
            // Logout from app.
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this@FacultyHomeActivity, LoginActivity::class.java))
            finish()
        }
        // END
    }
}