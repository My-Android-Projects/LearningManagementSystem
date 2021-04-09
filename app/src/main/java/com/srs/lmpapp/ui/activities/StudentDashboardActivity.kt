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
class StudentDashboardActivity : BaseActivity(), CourseSearchDataCommunicator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studentdashboard)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_mycourses,
                R.id.navigation_coursearch
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }

    override fun sendCourseFilterData(
        courseName: String,
        courseCategory: String,
        courseCredits: String,
        courseStartDate: String,
        courseEndDate: String
    ) {
        val bundle = Bundle()
        bundle.putString("courseName", courseName)
        bundle.putString("courseCategory", courseCategory)
        bundle.putString("courseCredits", courseCredits)
        bundle.putString("courseStartDate", courseStartDate)
        bundle.putString("courseEndDate", courseEndDate)
        val courseSearchResultFragment = CourseSearchResultFragment.newInstance(
        )
        val transaction = supportFragmentManager.beginTransaction()
        courseSearchResultFragment.arguments = bundle
        transaction.replace(R.id.frameLayout, courseSearchResultFragment).commit()
    }
}