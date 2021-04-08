package com.srs.lmpapp.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.srs.lmpapp.R
import com.srs.lmpapp.databinding.ActivityRegisterBinding
import com.srs.lmpapp.databinding.ActivityStudentHomeBinding
import com.srs.lmpapp.utils.Constants

class StudentHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityStudentHomeBinding.inflate(layoutInflater)
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

            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this@StudentHomeActivity, LoginActivity::class.java))
            finish()
        }

    }
}