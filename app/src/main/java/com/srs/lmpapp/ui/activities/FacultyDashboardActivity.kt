package com.srs.lmpapp.ui.activities

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.srs.lmpapp.R
import com.srs.lmpapp.`interface`.CourseSearchDataCommunicator
import com.srs.lmpapp.ui.fragments.CourseSearchResultFragment


/**
 *  Dashboard Screen of the app.
 */
class FacultyDashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_dashboard)
        val navView: BottomNavigationView = findViewById(R.id.nav_faculty_view)
        val navController = findNavController(R.id.nav_host_faculty_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_faculty_dashboard,
                R.id.navigation_faculty_mycourses,
                R.id.navigation_faculty_addcourses
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }


}